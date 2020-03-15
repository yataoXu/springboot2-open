package cn.myframe.utils.mlib;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
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

public class KMeansUtil2 {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("KMeansUtil").setMaster("spark://spark223.rojao.cn:7077");

        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
        jsc.setLogLevel("ERROR");

        // $example on$
        // Load and parse data
        String path = "hdfs://10.10.1.142:9000/nias/user.txt";
        JavaRDD<String>[] data = jsc.textFile(path).randomSplit(new double[]{0.01,0.98999999,0.00000001});

        JavaRDD<Row> rowJavaRDD = data[0].map(new Function<String, Row>() {
            @Override
            public Row call(String v1) throws Exception {
                String[] user = v1.split(",");
                if(user.length == 6){
                    return  RowFactory.create(user[0], Integer.parseInt(user[1]) ,user[2], user[3],user[4],user[5]);
                }
                return null;
            }
        });

        JavaRDD<Row> rowJavaRDD2 = data[2].map(new Function<String, Row>() {
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
        Dataset<Row> df2 = spark.createDataFrame(rowJavaRDD2, schema);

        df.show();
        System.out.println("===================================");

        StringIndexer workIndexer = new StringIndexer()
                .setInputCol("work")
                .setOutputCol("workIndex");

        StringIndexer areaIndexer = new StringIndexer()
                .setInputCol("area")
                .setOutputCol("areaIndex");

        StringIndexer sexIndexer = new StringIndexer()
                .setInputCol("sex")
                .setOutputCol("sexIndex");

        StringIndexer movieIndexer = new StringIndexer()
                .setInputCol("movie")
                .setOutputCol("movieIndex");



        OneHotEncoder workEncoder = new OneHotEncoder()
                .setInputCol(workIndexer.getOutputCol())
                .setOutputCol("workVec");

        OneHotEncoder areaEncoder = new OneHotEncoder()
                .setInputCol(areaIndexer.getOutputCol())
                .setOutputCol("areaVec");

        OneHotEncoder sexEncoder = new OneHotEncoder()
                .setInputCol(sexIndexer.getOutputCol())
                .setOutputCol("sexVec");

        OneHotEncoder encoder2 = new OneHotEncoder()
                .setInputCol("movieIndex")
                .setOutputCol("movieVec");


        VectorAssembler vectorAssembler  = new VectorAssembler().
                setInputCols(new String[]{"workVec","areaVec","sexVec"}).setOutputCol("featureVector");

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] {workIndexer,areaIndexer,sexIndexer,workEncoder, areaEncoder,sexEncoder,vectorAssembler});

        PipelineModel pipelineModel = pipeline.fit(df);


        Dataset<Row> predictions = pipelineModel.transform(df);
       // Dataset<Row> predictions2 = pipelineModel.transform(df2);
        predictions.show();


        KMeans kmeans = new KMeans().setK(20);
        kmeans.setFeaturesCol("featureVector");
        kmeans.setPredictionCol("pIndex");
        KMeansModel model = kmeans.fit(predictions);

        model.transform(predictions).select("name","pIndex").show();
      //  Dataset<Row> dateTest =model.transform(predictions2);

       /* Vector[] centers = model.clusterCenters();

        for (Vector center: centers) {
            System.out.println(center);
        }*/
       //

       // dateTest.show();


      //  KMeans kmeans = new KMeans().setK(500);
      //  kmeans.setFeaturesCol("featureVector");

       // predictions
    }
}
