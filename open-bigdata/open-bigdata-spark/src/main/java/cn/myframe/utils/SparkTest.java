package cn.myframe.utils;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SparkTest {

    static List<String> list = Arrays.asList("1,2,3,4,5","a,b,c,d,e","一,二,三,四,五");

    static List<String> unionList = Arrays.asList("A,B,C");

    static List<String> pairList = Arrays.asList("x,1","y,2","z,3","x,100","y,200","z,300");

    static List<String> joinList = Arrays.asList("x,a","y,b","z,c","x,一","y,二","z,三");

    public static void main(String[] args) {

     //   System.setProperty("hadoop.home.dir", "F:\\rdd");
        SparkConf conf = new SparkConf().setMaster("spark://spark223.rojao.cn:7077").setAppName("sparktest");;
        JavaSparkContext javaSparkContext = new JavaSparkContext(conf);
        javaSparkContext.addJar("F:\\admin\\project\\myframe\\myframe-bigdata\\myframe-spark\\target\\myframe-spark-1.0.0.jar");
        List<Integer> data = Arrays.asList(5, 1, 1, 4, 4, 2, 2);



        //创建并行集合的一个重要参数，是numSlices的数目），它指定了将数据集切分为几份。
        /*JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(data,3);
        javaRDD.foreach(new VoidFunction<Integer>() {
            @Override
            public void call(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });*/
        mapTest( javaSparkContext);
        //flatMapTest(javaSparkContext);
        //filterTest(javaSparkContext);
        //unionTest(javaSparkContext);
        //pairRDDTest(javaSparkContext);
        //groupByKeyTest(javaSparkContext);
        //reduceByKeyTest(javaSparkContext);
        //mapValueTest(javaSparkContext);
        //joinTest(javaSparkContext);
        //actionTest(javaSparkContext);
        //textFileTest(javaSparkContext);
        javaSparkContext.close();
    }

    public static void textFileTest(JavaSparkContext javaSparkContext){
        JavaRDD<String> javaRDD = javaSparkContext.textFile("file:/a.txt");
        JavaRDD<String> mapRdd =  javaRDD.map((s)->{
            return s;
        });
        javaRDD.collect().forEach((s)->{
            System.out.println(s);
        });
   //     System.out.println(JSON.toJSONString(javaRDD.collect()));
    }

    /**
     * 使用map生成新的JavaRDD
     *
     * 结果：[["1","2","3","4","5"],["a","b","c","d","e"],["一","二","三","四","五"]]
     * 每行数据操作后会保生成 一行 数据
     *
     * @param javaSparkContext
     */
    public static void mapTest(JavaSparkContext javaSparkContext){
        JavaRDD<String> javaRDD = javaSparkContext.parallelize(list);
        JavaRDD<String[]> mapRDD = javaRDD.map(new Function<String, String[]>() {
            @Override
            public String[] call(String s) throws Exception {
                return s.split(",");
            }
        });
       // System.out.println(JSON.toJSONString(mapRDD.collect()));
    }

    /**
     * 使用flatMap生成新的JavaRDD
     *
     * 结果：["1","2","3","4","5","a","b","c","d","e","一","二","三","四","五"]
     * 每行数据操作后会生成 多行 数据，而map操作只会生成一行
     *
     * @param javaSparkContext
     */
    public static void flatMapTest(JavaSparkContext javaSparkContext){
        JavaRDD<String> javaRDD = javaSparkContext.parallelize(list);
        JavaRDD<String> flatMapRDD = javaRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(",")).iterator();
            }
        });
       // System.out.println(JSON.toJSONString(flatMapRDD.collect()));
    }

    /**
     *  filter对 每行 数据执行过滤操作
     *
     *  结果：["a,b,c,d,e"]
     *
     * @param javaSparkContext
     */
    public static JavaRDD<String> filterTest(JavaSparkContext javaSparkContext){
        JavaRDD<String> javaRDD = javaSparkContext.parallelize(list);
        JavaRDD<String> filterRDD = javaRDD.filter(new Function<String, Boolean>() {
            //保留包含a的行
            @Override
            public Boolean call(String v1) throws Exception {
                if(v1.contains("a")){
                    return true;
                }
                return false;
            }
        });
        //System.out.println(JSON.toJSONString(filterRDD.collect()));
        return filterRDD;
    }

    /**
     * union操作对两个RDD数据进行合并
     *
     * 结果：["a,b,c,d,e","A,B,C"]
     *
     * @param javaSparkContext
     */
    public static void unionTest(JavaSparkContext javaSparkContext){
        JavaRDD<String> filterRDD = filterTest( javaSparkContext);
        JavaRDD<String> javaRDD = javaSparkContext.parallelize(unionList);
        JavaRDD<String> unionRdd = filterRDD.union(javaRDD);
        //System.out.println(JSON.toJSONString(unionRdd.collect()));
    }

    /**
     * 多存储键值对的数据格式
     * new Tuple2(elem1, elem2) 来创建一个新的二元组
     *
     * 结果：[{"_1":"x","_2":1},{"_1":"y","_2":2},{"_1":"z","_2":3},{"_1":"x","_2":100},{"_1":"y","_2":200},{"_1":"z","_2":300}]
     * @param javaSparkContext
     */
    public static JavaPairRDD<String,Integer> pairRDDTest(JavaSparkContext javaSparkContext){
        JavaRDD javaRDD = javaSparkContext.parallelize(pairList);
        JavaPairRDD<String,Integer> pairRDD = javaRDD.mapToPair((s)->{
            String[] array = s.toString().split(",");
            return new Tuple2(array[0],Integer.parseInt(array[1]));
        });
       // System.out.println(JSON.toJSONString(pairRDD.collect()));
       /* JavaPairRDD<String,Integer> mapRDD = javaRDD.mapToPair(new PairFunction() {
            @Override
            public Tuple2 call(Object o) throws Exception {
                return null;
            }
        });*/

       return pairRDD;
    }

    /**
     *  groupByKey对pair中的key进行group by操作
     *
     *  结果：[{"_1":"z","_2":[300,3]},{"_1":"x","_2":[100,1]},{"_1":"y","_2":[2,200]}]
     */
    public static void groupByKeyTest(JavaSparkContext javaSparkContext){
        JavaPairRDD<String,Integer> pairRDD =  pairRDDTest(javaSparkContext);
        JavaPairRDD<String, Iterable<Integer>> groupRDD = pairRDD.groupByKey();
       // System.out.println(JSON.toJSONString(groupRDD.collect()));
    }

    /**
     * 对pair中的key先进行group by操作，然后根据函数对聚合数据后的数据操作
     *
     * 结果：[{"_1":"z","_2":303},{"_1":"x","_2":101},{"_1":"y","_2":202}]
     * @param javaSparkContext
     */
    public static void reduceByKeyTest(JavaSparkContext javaSparkContext){
        JavaPairRDD<String,Integer> pairRDD =  pairRDDTest(javaSparkContext);
        JavaPairRDD<String, Integer> groupRDD = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1+v2;
            }
        });
       // System.out.println(JSON.toJSONString(groupRDD.collect()));
    }

    /**
     * 对value进行操作，生成新的JavaPairRDD
     *
     * 结果：[{"_1":"x","_2":"new1"},{"_1":"y","_2":"new2"},{"_1":"z","_2":"new3"},{"_1":"x","_2":"new100"},{"_1":"y","_2":"new200"},{"_1":"z","_2":"new300"}]
     * @param javaSparkContext
     */
    public static void mapValueTest(JavaSparkContext javaSparkContext){
        JavaPairRDD<String,Integer> pairRDD =  pairRDDTest(javaSparkContext);
        JavaPairRDD<String, String> mapValuesRDD = pairRDD.mapValues(new Function<Integer, String>() {
            @Override
            public String call(Integer v1) throws Exception {
                return "new"+v1;
            }
        });
      //  System.out.println(JSON.toJSONString(mapValuesRDD.collect()));
    }

    /**
     * join与sql中join含义一致，将两个RDD中key一致的进行join连接操作
     *
     *  结果： [{"_1":"z","_2":{"_1":"300","_2":"c"}},{"_1":"z","_2":{"_1":"300","_2":"三"}},{"_1":"z","_2":{"_1":"3","_2":"c"}},{"_1":"z","_2":{"_1":"3","_2":"三"}},{"_1":"x","_2":{"_1":"100","_2":"a"}},{"_1":"x","_2":{"_1":"100","_2":"一"}},{"_1":"x","_2":{"_1":"1","_2":"a"}},{"_1":"x","_2":{"_1":"1","_2":"一"}},{"_1":"y","_2":{"_1":"2","_2":"二"}},{"_1":"y","_2":{"_1":"2","_2":"b"}},{"_1":"y","_2":{"_1":"200","_2":"二"}},{"_1":"y","_2":{"_1":"200","_2":"b"}}]
     * @param javaSparkContext
     */
    public static   void joinTest(JavaSparkContext javaSparkContext){
        JavaRDD javaRDD = javaSparkContext.parallelize(pairList);
        JavaRDD javaRDD2 = javaSparkContext.parallelize(joinList);
        JavaPairRDD<String,String> pairRDD = javaRDD.mapToPair((s)->{
            String[] array = s.toString().split(",");
            return new Tuple2(array[0],array[1]);
        });
        JavaPairRDD<String,String> pairRDD2 = javaRDD2.mapToPair((s)->{
            String[] array = s.toString().split(",");
            return new Tuple2(array[0],array[1]);
        });
        JavaPairRDD<String,Tuple2<String,String>> joinRDD = pairRDD.join(pairRDD2);
        //System.out.println(JSON.toJSONString(joinRDD.collect()));
    }

    /**
     *
     * RDD数据输出
     *
     * @param javaSparkContext
     */
    public static void actionTest(JavaSparkContext javaSparkContext){
        JavaRDD javaRDD = javaSparkContext.parallelize(pairList);
        //统计输出数据行数
        System.out.println(javaRDD.count());
        //输出所有输出数据
        System.out.println(javaRDD.collect());

        javaRDD.saveAsTextFile("file:/D:/a.txt");
    }







}
