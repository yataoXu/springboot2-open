package cn.myframe.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  spark-submit --class cn.rojao.utils.SparkHbaseUtil  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class SparkHbaseUtil {

    public static void main(String[] args) {

        SparkSession spark=SparkSession.builder()
                .appName("lcc_java_read_hbase_register_to_table")
                .master("spark://spark223.rojao.cn:7077")
                .getOrCreate();

        JavaSparkContext context = new JavaSparkContext(spark.sparkContext());

        context.setLogLevel("ERROR");
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "10.10.1.141");
        //configuration.set("hbase.master", "192.168.10.82:60000");

        Scan scan = new Scan();
        String tableName = "test_lcc_person";
        configuration.set(TableInputFormat.INPUT_TABLE, tableName);

        ClientProtos.Scan proto = null;
        try {
            proto = ProtobufUtil.toScan(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ScanToString = Base64.encodeBytes(proto.toByteArray());
        configuration.set(TableInputFormat.SCAN, ScanToString);


        JavaPairRDD<ImmutableBytesWritable, Result> myRDD = context.newAPIHadoopRDD(configuration, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

        JavaRDD<Row> personsRDD = myRDD.map(new Function<Tuple2<ImmutableBytesWritable,Result>,Row>() {

            @Override
            public Row call(Tuple2<ImmutableBytesWritable, Result> tuple) throws Exception {
                // TODO Auto-generated method stub
                System.out.println("====tuple=========="+tuple);
                Result result = tuple._2();
                String rowkey = Bytes.toString(result.getRow());
                String name = Bytes.toString(result.getValue(Bytes.toBytes("lcc_liezu"), Bytes.toBytes("name")));
                String sex = Bytes.toString(result.getValue(Bytes.toBytes("lcc_liezu"), Bytes.toBytes("sex")));
                String age = Bytes.toString(result.getValue(Bytes.toBytes("lcc_liezu"), Bytes.toBytes("age")));
                //这一点可以直接转化为row类型
                return (Row) RowFactory.create(rowkey,name,sex,age);
            }
        });

        List<StructField> structFields=new ArrayList<StructField>();
        structFields.add(DataTypes.createStructField("id", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("sex", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("age", DataTypes.StringType, true));

        StructType schema=DataTypes.createStructType(structFields);

        Dataset stuDf=spark.createDataFrame(personsRDD, schema);
        //stuDf.select("id","name","age").write().mode(SaveMode.Append).parquet("par");
        stuDf.printSchema();
        stuDf.createOrReplaceTempView("Person");
        Dataset<Row> nameDf=spark.sql("select * from Person ");
        nameDf.show();




    }
}
