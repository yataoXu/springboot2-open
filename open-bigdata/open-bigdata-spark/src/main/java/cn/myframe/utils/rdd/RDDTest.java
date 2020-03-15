package cn.myframe.utils.rdd;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

/**
 * ./spark-submit --class cn.rojao.utils.rdd.RDDTest  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class RDDTest {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("JavaHdfsLR")
                .getOrCreate();

        JavaSparkContext context = new JavaSparkContext(spark.sparkContext());
        context.setLogLevel("ERROR");

        JavaRDD<String> lines = spark.read().textFile("hdfs://10.10.1.142:9000/nias/user.txt").javaRDD();
        JavaRDD<String> rdd = lines.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String v1) throws Exception {
                if(v1.contains("学生")){
                    return true;
                }
                return false;
            }
        });
        //实际上是data.persist(StorageLevel.MEMORY_ONLY)
        rdd.cache();// ==  rdd.persist(StorageLevel.MEMORY_ONLY());

        System.out.println("------------------------");
        System.out.println(rdd.count());
        Long startTime = System.currentTimeMillis();
        System.out.println(rdd.count());
        System.out.println(System.currentTimeMillis()-startTime);



    }
}
