package cn.myframe.utils;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparkUtil {

  //  static Logger logger = Logger.getLogger(SparkUtil.class.getName());


    private final static Logger logger = LoggerFactory.getLogger(SparkUtil.class);

    public static void main(String[] args) {

        String log4j = SparkUtil.class.getClassLoader().getResource("log4j.properties").getPath();
        PropertyConfigurator.configure(log4j);//加载.properties文件


        logger.debug("XXX {}", "33333333");


      //  Logger.getLogger("org").setLevel(Level.ERROR);
        try{
          /*  SparkConf sparkConf = new SparkConf().setMaster("spark://spark223.rojao.cn:7077").setAppName("JavaWordCount_test");
            JavaSparkContext ctx = new JavaSparkContext(sparkConf);
            ctx.setLogLevel("ERROR");
            ctx.addJar("F:\\admin\\project\\myframe\\myframe-bigdata\\myframe-spark\\target\\myframe-spark-1.0.0.jar");
           // ctx.setJars(new String[]{"/soft/dounine/github/spark-learn/build/libs/spark-learn-1.0-SNAPSHOT.jar"})
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
            //初始化Rdd
            JavaRDD<Integer> rdd = ctx.parallelize(list);
            //第二和第三个参数为函数的匿名实现（lambda形式 ）
            Tuple2<Double, Integer> t = rdd.aggregate(new Tuple2<Double, Integer>(0.0, 0),
                    (x,y)->new Tuple2<Double, Integer>(x._1+y,x._2+1),
                    (x,y)->new Tuple2<Double, Integer>(x._1+y._1,x._2+y._2));
            System.err.println(t._1/t._2);*/
           // log.error("---------------------"+(t._1/t._2));
           // ctx.close();
            //logger.debug("11");
        }catch (Exception e){
            e.printStackTrace();
        }




    }
}
