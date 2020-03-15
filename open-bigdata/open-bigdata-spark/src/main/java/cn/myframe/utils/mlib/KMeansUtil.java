package cn.myframe.utils.mlib;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.OneHotEncoder;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;

/**
 * spark-submit --class cn.rojao.utils.mlib.KMeansUtil  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class KMeansUtil {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("KMeansUtil").setMaster("spark://spark223.rojao.cn:7077");

        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
        jsc.setLogLevel("ERROR");

        // $example on$
        // Load and parse data
        String path = "hdfs://10.10.1.142:9000/nias/user.txt";
        JavaRDD<String> data = jsc.textFile(path);

        JavaRDD<Row> rowJavaRDD = data.map(new Function<String, Row>() {
            @Override
            public Row call(String v1) throws Exception {
                String[] user = v1.split(",");
                if(user.length == 6){
                    return  RowFactory.create(user[0], Integer.parseInt(user[1]) ,user[2], user[3],user[4],user[5]);
                }
                return null;
            }
        });

        StructType schema = new StructType(new StructField[]{
                new StructField("name", DataTypes.StringType, false, Metadata.empty()),
                new StructField("age", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("work", DataTypes.StringType, false, Metadata.empty()),
                new StructField("area", DataTypes.StringType, false, Metadata.empty()),
                new StructField("sex", DataTypes.StringType, false, Metadata.empty()),
                new StructField("movie", DataTypes.StringType, false, Metadata.empty())
        });

        Dataset<Row> df = spark.createDataFrame(rowJavaRDD, schema);

        StringIndexerModel indexer = new StringIndexer()
                .setInputCol("work")
                .setOutputCol("workIndex")
                .fit(df);
        Dataset<Row> indexed = indexer.transform(df);
        OneHotEncoder encoder = new OneHotEncoder()
                .setInputCol("workIndex")
                .setOutputCol("categoryVec");
        Dataset<Row> encoded = encoder.transform(indexed);
        encoded.show();

        System.out.println("-----------------------------------------------");
        StringIndexerModel indexer2 = new StringIndexer()
                .setInputCol("movie")
                .setOutputCol("movieIndex")
                .fit(encoded);
        Dataset<Row> indexed2 = indexer2.transform(encoded);
        OneHotEncoder encoder2 = new OneHotEncoder()
                .setInputCol("movieIndex")
                .setOutputCol("movieVec");
        Dataset<Row> encoded2 = encoder2.transform(indexed2);
        encoded2.show();


        VectorAssembler vectorAssembler  = new VectorAssembler().
                setInputCols(new String[]{"categoryVec","movieVec"}).setOutputCol("featureVector");


        Dataset<Row> encoded3 = vectorAssembler.transform(encoded2);
        encoded3.show();


        KMeans kmeans = new KMeans().setK(500);
        kmeans.setFeaturesCol("featureVector");
        KMeansModel model = kmeans.fit(encoded3);

        Vector[] centers = model.clusterCenters();
        System.out.println("Cluster Centers: ");

        try {
            model.save("hdfs://10.10.1.142:9000/nias/kmeans");
        } catch (IOException e) {
            e.printStackTrace();
        }
        encoded3.foreach(new ForeachFunction<Row>() {
            @Override
            public void call(Row row) throws Exception {
                System.out.println("聚合中心："+model.predict(row.getAs("featureVector")));
            }
        });
        /*for (Vector center: centers) {
            System.out.println(center);
        }*/
       // model.predict();
        /*OneHotEncoder encoder = new OneHotEncoder()
                .setInputCol("categoryIndex")
                .setOutputCol("categoryVec");*/

    }
}
