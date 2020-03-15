package cn.myframe.utils;

import org.apache.spark.deploy.SparkSubmit;

/**
 * 提交任务
 */
public class SparkSubmitUtil {

    public static void main(String[] args) {
        /*SparkJobLog jobLog = new SparkJobLog();
        jobLog.setExecTime(new Date());*/
        String jarPath = "F:\\admin\\project\\myframe\\myframe-bigdata\\myframe-spark\\target\\myframe-spark-1.0.0.jar";
        String sparkUri ="spark://spark223.rojao.cn:7077";
        String[] arg0=new String[]{
                jarPath,
                "--class","cn.rojao.utils.mlib.DataSetUtil",
                "--master",sparkUri,
                "--name","web polling",
                "--executor-memory","1G"
        };

        SparkSubmit.main(arg0);

    }
}
