package cn.myframe.utils;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.hive.HiveContext;

public class SparkHiveUtil {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("spark://spark223.rojao.cn:7077").setAppName("JavaWordCount_test");;
        JavaSparkContext sc = new JavaSparkContext(conf);
        HiveContext hiveContext = new HiveContext(sc.sc());
       // Dataset<Row> rows = hiveContext.sql("select * from xx.table_name where ds = 'dd'");
    }
}
