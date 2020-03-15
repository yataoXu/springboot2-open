package cn.myframe.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.*;

/**
 *  spark-submit --class cn.rojao.utils.SparkKafkaUtil  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class SparkKafkaUtil {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("demo").setMaster("spark://spark223.rojao.cn:7077");

        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        javaSparkContext.setLogLevel("ERROR");

        JavaStreamingContext streamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(60));

        Map<String, Object> kafkaParams = new HashMap<>();
        //Kafka服务监听端口
        kafkaParams.put("bootstrap.servers", "10.10.2.138:9092,10.10.2.138:9093,10.10.2.138:9094");
        //指定kafka输出key的数据类型及编码格式（默认为字符串类型编码格式为uft-8）
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        //指定kafka输出value的数据类型及编码格式（默认为字符串类型编码格式为uft-8）
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        //消费者ID，随意指定
        kafkaParams.put("group.id", "jis");
        //指定从latest(最新,其他版本的是largest这里不行)还是smallest(最早)处开始读取数据
        kafkaParams.put("auto.offset.reset", "latest");
        //如果true,consumer定期地往zookeeper写入每个分区的offset
        kafkaParams.put("enable.auto.commit", false);
        kafkaParams.put("connections.max.idle.ms",1000);

        //要监听的Topic，可以同时监听多个
        Collection<String> topics = Arrays.asList("t1","t2");
         Map<TopicPartition, Long> offsets = new HashMap<>();
         offsets.put(new TopicPartition("t1", 0), 2L);
         JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams/*,offsets*/)
                );

        JavaPairDStream pairDS = stream.flatMap(x -> {
                                        System.out.println(x);
                                        return Arrays.asList(x.value().toString().split(" ")).iterator();
                                    })
                                    .mapToPair(x -> {
                                        System.out.println(x);
                                        return new Tuple2<String, Integer>(x, 1);
                                    }).reduceByKey((x, y) -> x + y);
        pairDS.print();

        streamingContext.start();
        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






















    public static void main2(String[] args) {

        SparkConf conf = new SparkConf();
//		conf.setMaster("local[4]");
        conf.setMaster("spark://spark223.rojao.cn:7077");
        conf.setAppName("transfer App");
        conf.set("spark.streaming.stopGracefullyOnShutdown","true");
        conf.set("spark.default.parallelism", "6");

        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
      //  javaSparkContext.setLogLevel("ERROR");
      //  javaSparkContext.addJar("F:\\admin\\project\\myframe\\myframe-bigdata\\myframe-spark\\target\\myframe-spark-1.0.0.jar");
        JavaStreamingContext jssc = new JavaStreamingContext(javaSparkContext, Durations.seconds(5));

        System.out.println("-----------------------------------------------------------------------------------");
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "10.10.2.138:9093");
     //   kafkaParams.put("key.serializer", StringSerializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "group1");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", true);
        kafkaParams.put("connections.max.idle.ms",1000);

        Collection<String> topic0 = Arrays.asList("self-topic0");
        Collection<String> topic1 = Arrays.asList("self-topic1");
        Collection<String> topic2 = Arrays.asList("self-topic2");
        Collection<String> topic3 = Arrays.asList("self-topic3");
        Collection<String> topic4 = Arrays.asList("self-topic4");
        Collection<String> topic5 = Arrays.asList("self-topic5");
        List<Collection<String>> topics = Arrays.asList(topic0, topic1, topic2, topic3, topic4, topic5);
       // List<JavaDStream<ConsumerRecord<String, String>>> kafkaStreams = new ArrayList<>(topics.size());



       /* for (int i = 0; i < topics.size(); i++) {
            kafkaStreams.add(KafkaUtils.createDirectStream(
                    jssc,
                    LocationStrategies.PreferConsistent(),
                    ConsumerStrategies.<String, String>Subscribe(topics.get(i), kafkaParams)));
        }
        JavaDStream<ConsumerRecord<String, String>> stream = jssc.union(kafkaStreams.get(0), kafkaStreams.subList(1, kafkaStreams.size()));
*/
        //stream.foreachRDD((rdd)->{rdd.foreachPartition((crs)->/*patchTransfer(crs)*/System.out.println(crs));});


        /*stream.foreachRDD(new VoidFunction<JavaRDD<ConsumerRecord<String, String>>>() {
            @Override
            public void call(JavaRDD<ConsumerRecord<String, String>> consumerRecordJavaRDD) throws Exception {
                consumerRecordJavaRDD.foreachPartition(new VoidFunction<Iterator<ConsumerRecord<String, String>>>() {
                    @Override
                    public void call(Iterator<ConsumerRecord<String, String>> consumerRecordIterator) throws Exception {
                        System.out.println("11111111111111111");
                        if(consumerRecordIterator.hasNext()){
                            System.out.println(consumerRecordIterator.next());
                        }
                    }
                });
            }
        });*/

        Collection<String> topicsSet = new HashSet<>(Arrays.asList("t1".split(",")));
        //Topic分区
        Map<TopicPartition, Long> offsets = new HashMap<>();
        offsets.put(new TopicPartition("t_spark1", 0), 2L);
        JavaInputDStream<ConsumerRecord<String,String>> lines = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams/*,offsets*/)
        );

      /*  lines.mapToPair(
            new PairFunction<ConsumerRecord<String, String>, String, String>() {
                @Override
                public Tuple2<String, String> call(ConsumerRecord<String, String> record) {
                    System.out.println(555555);
                    System.out.println(record.key());
                    System.out.println(record.value());
                    return new Tuple2<>(record.key(), record.value());
                }
        });*/

        /*lines.foreachRDD(new VoidFunction<JavaRDD<ConsumerRecord<String, String>>>() {

            @Override
            public void call(JavaRDD<ConsumerRecord<String, String>> consumerRecordJavaRDD) throws Exception {

                System.out.println(111);
                final OffsetRange[] offsetRanges = ((HasOffsetRanges) consumerRecordJavaRDD.rdd()).offsetRanges();
                consumerRecordJavaRDD.foreachPartition(new VoidFunction<Iterator<ConsumerRecord<String, String>>>() {
                    @Override
                    public void call(Iterator<ConsumerRecord<String, String>> consumerRecords) {
                        System.out.println(222);
                        OffsetRange o = offsetRanges[TaskContext.get().partitionId()];
                        System.out.println(
                                o.topic() + " " + o.partition() + " " + o.fromOffset() + " " + o.untilOffset());
                    }
                });

            }
        });*/

        //这里就跟之前的demo一样了，只是需要注意这边的lines里的参数本身是个ConsumerRecord对象
        JavaPairDStream<String, Integer> counts =
                lines.flatMap(x -> {
                    System.out.println(x);
                    return Arrays.asList(x.value().toString().split(" ")).iterator();
                })
                .mapToPair(x -> {
                    System.out.println(x);
                    return new Tuple2<String, Integer>(x, 1);
                }).reduceByKey((x, y) -> x + y);
        counts.print();


        System.out.println("-------------end-----------");
        lines.foreachRDD(rdd -> {
          rdd.foreach(x -> {
              System.out.println("11");
            System.out.println(x);
          });
       });


        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      //  jssc.close();
        System.out.println("-------------end2-----------");
    }





}
