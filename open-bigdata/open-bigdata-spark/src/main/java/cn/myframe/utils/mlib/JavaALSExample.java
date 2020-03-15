package cn.myframe.utils.mlib;


import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.col;

// $example on$
import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;

/**
 * ALS交替最小二乘法
 * spark-submit --class cn.rojao.utils.mlib.JavaALSExample  /opt/spark-demo/myframe-spark-1.0.0.jar
 */
public class JavaALSExample {
    // $example on$
    public static class Rating implements Serializable {
        private int userId;
        private int movieId;
        private float rating;
        private long timestamp;

        public Rating() {}

        public Rating(int userId, int movieId, float rating, long timestamp) {
            this.userId = userId;
            this.movieId = movieId;
            this.rating = rating;
            this.timestamp = timestamp;
        }

        public int getUserId() {
            return userId;
        }

        public int getMovieId() {
            return movieId;
        }

        public float getRating() {
            return rating;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public static Rating parseRating(String str) {
            String[] fields = str.split("::");
            if (fields.length != 4) {
                throw new IllegalArgumentException("Each line must contain 4 fields");
            }
            int userId = Integer.parseInt(fields[0]);
            int movieId = Integer.parseInt(fields[1]);
            float rating = Float.parseFloat(fields[2]);
            long timestamp = Long.parseLong(fields[3]);
            return new Rating(userId, movieId, rating, timestamp);
        }
    }
    // $example off$

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaALSExample")
                .getOrCreate();

        JavaSparkContext context = new JavaSparkContext(spark.sparkContext());
        context.setLogLevel("ERROR");

        // $example on$
        JavaRDD<Rating> ratingsRDD = spark
                .read().textFile("hdfs://10.10.1.142:9000/nias/sample_movielens_ratings.txt").javaRDD()
                .map(new Function<String, Rating>() {
                    public Rating call(String str) {
                        return Rating.parseRating(str);
                    }
                });
        Dataset<Row> ratings = spark.createDataFrame(ratingsRDD, Rating.class);
        //函数根据weights权重，将一个RDD切分成多个RDD
        Dataset<Row>[] splits = ratings.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> training = splits[0];
        Dataset<Row> test = splits[1];

        //使用训练数据训练模型
        //这里的ALS是import org.apache.spark.ml.recommendation.ALS，不是mllib中的哈
        //setMaxiter设置最大迭代次数
        //setRegParam设置正则化参数，日lambda这个不是更明显么
        //setUserCol设置用户id列名
        //setItemCol设置物品列名
        //setRatingCol设置打分列名
        ALS als = new ALS()
                .setMaxIter(5)
                .setRegParam(0.01)
                .setUserCol("userId")
                .setItemCol("movieId")
                .setRatingCol("rating");

        //fit给输出的数据，训练模型，fit返回的是ALSModel类
        ALSModel model = als.fit(training);

        //使用测试数据计算模型的误差平方和
        //transform方法把数据dataset换成dataframe类型，预测数据
        Dataset<Row> predictions = model.transform(test);

        //RegressionEvaluator这个类是用户评估预测效果的，预测值与原始值
        //这个setLabelCol要和als设置的setRatingCol一致，不然会报错哈
        //RegressionEvaluator的setPredictionCol必须是prediction因为，ALSModel的默认predictionCol也是prediction
        //如果要修改的话必须把ALSModel和RegressionEvaluator一起修改
        //model.setPredictionCol("prediction")和evaluator.setPredictionCol("prediction")
        //setMetricName这个方法，评估方法的名字，一共有哪些呢？
        //rmse-平均误差平方和开根号
        //mse-平均误差平方和
        //mae-平均距离（绝对）
        //r2-没用过不知道
        //这里建议就是用rmse就好了，其他的基本都没用，当然还是要看应用场景，这里是预测分值就是用rmse。如果是预测距离什么的mae就不从，看场景哈
        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setMetricName("rmse")
                .setLabelCol("rating")
                .setPredictionCol("prediction");
        Double rmse = evaluator.evaluate(predictions);
        System.out.println("Root-mean-square error = " + rmse);
        // $example off$

       /* try {
            model.save("hdfs://10.10.1.142:9000/nias/model");
        } catch (IOException e) {
            e.printStackTrace();
        }*/


       // Column column  = new Column("");
     //   column.
        Dataset datasetM = model.itemFactors().select(col("id").as("movieId")).withColumn("userId",lit(3));
        Dataset dataset = model.transform(datasetM).select("userId", "movieId").orderBy(col("prediction").desc()).limit(10);
        dataset.printSchema();
        dataset.show();
       // System.out.println(dataset.collect());
        spark.stop();
    }
}
