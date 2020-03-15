package cn.myframe.utils.mlib;

import org.apache.spark.SparkContext;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

/**
 * spark-submit --class cn.rojao.utils.mlib.ALSModelCreater  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class ALSModelCreater {

    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder().appName("Spark889").master("spark://spark223.rojao.cn:7077").getOrCreate();
        SparkContext javaSparkContext = spark.sparkContext();

        //javaSparkContext.setLogLevel("ERROR");
        MatrixFactorizationModel model=MatrixFactorizationModel.load(javaSparkContext,"hdfs://10.10.1.142:9000/nias/model");
        System.out.println(Arrays.toString(model.recommendProducts(4,10)));

    }
}
