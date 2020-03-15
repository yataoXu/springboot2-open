package cn.myframe.utils;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.*;

/**
 *  spark-submit --class cn.rojao.utils.SparkMysqlUtil  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class SparkMysqlUtil {

    public static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SparkMysqlUtil.class);

    public static void main(String[] args) {
        final Set<String> set = new HashSet<String>();
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://10.10.2.137/spark?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8";
        Properties properties = new Properties();
        properties.put("user","root");
        properties.put("password","Rojao@123");
        properties.put("driver","com.mysql.jdbc.Driver");

        SparkSession spark = SparkSession.builder().appName("Spark2").master("spark://spark223.rojao.cn:7077").getOrCreate();
        SparkContext javaSparkContext = spark.sparkContext();
        javaSparkContext.addJar("F:\\admin\\project\\myframe\\myframe-bigdata\\myframe-spark\\target\\myframe-spark-1.0.0.jar");
        JavaRDD<String> input = javaSparkContext
                .textFile("hdfs://10.10.1.142:9000/nias/receiver.txt",1).toJavaRDD().map(new Function<String, String>() {
                    public String call(String s) throws Exception {
                        logger.info("---------------------------------------"+s);
                        /*String[] tmps = s.split("\t");
                        if(tmps.length == 21){
                            String label = tmps[16] + "\t" + tmps[19] + "\t" + tmps[5] + "\t" + tmps[17] + "\t" + tmps[6]
                                    + "\t" + tmps[9] + "\t" + tmps[4];
                            if(!set.contains(label)){
                                set.add(label);
                                return s;
                            }

                        }*/
                        set.add( s);
                        return s;
                      //  return null;
                    }
                });
        JavaRDD<Row> inputRows = input.filter(new Function<String, Boolean>() {

            public Boolean call(String s) throws Exception {
                if(null == s){
                    return false;
                }
               // String[] strs = s.split("\t");
                /*if("".equals(strs[14])){
                    return false;
                }*/
                return true;
            }
        }).map(new Function<String, Row>() {

            public Row call(String s) throws Exception {
                String[] tmp = s.split("\t");
                return RowFactory.create(Long.valueOf(tmp[0]),tmp[1],tmp[2]/*,Integer.valueOf(tmp[3]),
                        tmp[4],tmp[5],tmp[6],tmp[7],tmp[8],tmp[9],tmp[10],Integer.valueOf(tmp[11]),tmp[12],
                        tmp[13],Integer.valueOf(tmp[14]),tmp[15],
                        tmp[16],tmp[17],Integer.valueOf(tmp[18]),tmp[19],tmp[20]*/);
            }
        });
        List structFields = new ArrayList();
        structFields.add(DataTypes.createStructField("id",DataTypes.LongType,true));
        structFields.add(DataTypes.createStructField("name",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("address",DataTypes.StringType,true));
       /*  structFields.add(DataTypes.createStructField("version",DataTypes.IntegerType,true));
        structFields.add(DataTypes.createStructField("addition",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("bookname",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("content",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("dversion",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("grade",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("message",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("operatedate",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("pageindex",DataTypes.IntegerType,true));
        structFields.add(DataTypes.createStructField("realname",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("school",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("source",DataTypes.IntegerType,true));
        structFields.add(DataTypes.createStructField("status",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("time",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("type",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("week",DataTypes.IntegerType,true));
        structFields.add(DataTypes.createStructField("userid",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("caption",DataTypes.StringType,true));*/

        StructType structType = DataTypes.createStructType(structFields);
        Dataset<Row> ds = spark.createDataFrame(inputRows,structType);
        ds.write().mode("append").jdbc(url,"bus_receiver",properties);
        spark.close();



    }

}
