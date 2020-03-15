package cn.myframe.utils.mlib;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.feature.Binarizer;
import org.apache.spark.ml.feature.Bucketizer;
import org.apache.spark.ml.feature.QuantileDiscretizer;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import static org.apache.spark.sql.functions.callUDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  spark-submit --class cn.rojao.utils.mlib.DataSetUtil  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class DataSetUtil {

    static List<String> list = Arrays.asList("1,2,3,4,5","a,b,c,d,e","一,二,三,四,五");

    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder().appName("Spark889").master("spark://spark223.rojao.cn:7077").getOrCreate();

        JavaSparkContext context = new JavaSparkContext(spark.sparkContext());
        context.setLogLevel("ERROR");

        /*Dataset dataset = spark.range(1000,10080);
        dataset.show();
        dataset.describe().show();


        Encoder<Person> personEncoder = Encoders.bean(Person.class);
        List<Person> personList = Arrays.asList(new Person("小明",1),
        new Person("小a",11),new Person(null,12));
        Dataset dataset1 = spark.createDataset(personList,personEncoder);
        dataset1.show();
        dataset1.printSchema();

        Dataset<Row> personDF = spark.createDataFrame(personList, Person.class);
        System.out.println("3. 直接构建出 Dataset<Row>");
        personDF.show();
        personDF.printSchema();*/

       /* Dataset dataset3 = personDF.withColumn("money",lit(100));

        Map<String,Object> fillMap = new  HashMap<>();
        fillMap.put("name","无名");
        fillMap.put("work","其它");
        dataset3.na().fill(fillMap);
        dataset3.show();*/


        System.out.println("-------------vectorAssembler--------------------------");
       // System.out.println(dataset3.columns());
        /**
         * 部分列的数据转换为特征向量，并统一命名，VectorAssembler类完成这一任务。
         * VectorAssembler是一个transformer，将多列数据转化为单列的向量列
         * 只支持类型**/
        /*VectorAssembler vectorAssembler  = new VectorAssembler().
                setInputCols(new String[]{"age"}).setOutputCol("featureVector");
        Dataset ds = vectorAssembler.transform(dataset3);
        ds.show();*/


       // binarizerTest(  spark );
       // bucketizerTest(spark);
       /* quantileDiscretizerTest(spark);
        Seq seq = scala.collection.JavaConversions.asScalaBuffer(list);*/
     //   Seq<String> seq = CommonUtil.columnNames("productId");
       // spark.createDataFrame(list,Person.class)

        List<String> list = Arrays.asList("1,0.1,1.3,4.4");
        JavaRDD<Row> mapRDD = context.parallelize(list).map(new Function<String, Row>() {
            @Override
            public Row call(String v1) throws Exception {
                String[] tmp = v1.split(",");
                return RowFactory.create(Long.valueOf(tmp[0]),Double.parseDouble(tmp[1]),Double.parseDouble(tmp[2]),Double.parseDouble(tmp[3]),Vectors.dense(10.0, 0.1, -8.0));
            }
        });
        List structFields = new ArrayList();
        structFields.add(DataTypes.createStructField("id",DataTypes.LongType,true));
        structFields.add(DataTypes.createStructField("f1",DataTypes.DoubleType,true));
        structFields.add(DataTypes.createStructField("f2",DataTypes.DoubleType,true));
        structFields.add(DataTypes.createStructField("f3",DataTypes.DoubleType,true));
        structFields.add(DataTypes.createStructField("f4",DataTypes.DoubleType,true));
      //  structFields.add(DataTypes.createStructField("f4",DataTypes.,true));
        StructType structType = DataTypes.createStructType(structFields);
        Dataset ds =  spark.createDataFrame(mapRDD,structType);
        ds.show();
       // Dataset ds2 =  ds.select(callUDF("id",col("f1"),col("f2")));
        //ds2.show();
       // Vectors.dense(10.0, 0.1, -8.0);
     //   spark.createDataFrame(seq).toDF("id", "features");
    }

    /**
     * 根据阈值进行二值化,大于阈值的为1.0,小于等于阈值的为0.0
     *
     *  结果age大于50的转为1，小于50的转为0
     *
     * @param spark
     */
    public static void binarizerTest( SparkSession spark){
        List<Person> list = Arrays.asList(
                new Person("a",10),
                new Person("b",50),
                new Person("c",60));
        Dataset dataset = spark.createDataFrame(list,Person.class);
        Dataset ds = new Binarizer().setThreshold(50.0).setInputCol("age").setOutputCol("binarizer_value")
                .transform(dataset);
        ds.show();
    }

    /**
     *  连续型数据处理之给定边界离散化
     * @param spark
     */
    public static void bucketizerTest(SparkSession spark){
        List<Person> list = Arrays.asList(
                new Person("a",10),
                new Person("a",25),
                new Person("b",40),
                new Person("c",60));
        Dataset dataset = spark.createDataFrame(list,Person.class);
        // 设定边界，分为5个年龄组：[0,20),[20,40),[40,80),[80,正无穷)
        double[] split = new double[]{0.0,20.0,40.0,80.0,Double.MAX_VALUE};
        Dataset ds = new Bucketizer().setSplits(split).setInputCol("age").setOutputCol("bucketizer_value")
                .transform(dataset);
        ds.show();
    }

    /**
     * 连续型数据处理之给定分位数离散化
     * @param spark
     */
    public static void quantileDiscretizerTest(SparkSession spark){
        List<Person> list = Arrays.asList(
                new Person("a",10),
                new Person("a",25),
                new Person("b",40),
                new Person("c",60));
        Dataset dataset = spark.createDataFrame(list,Person.class);
        // 但这里不再自己定义splits（分类标准），而是定义分几箱就可以了,这里分10箱
        Dataset ds = new QuantileDiscretizer().setNumBuckets(10).setInputCol("age")
                .setOutputCol("quantileDiscretizer_value")
                .fit(dataset)
                .transform(dataset);
        ds.show();
    }



}



