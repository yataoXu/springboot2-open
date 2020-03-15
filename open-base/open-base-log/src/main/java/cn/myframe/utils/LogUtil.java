package cn.myframe.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 8:48
 */
public class LogUtil {

    /**
     * 返回日志数量
     * @return
     */
    public static int logCount(){
        LoggerContext loggerContext= (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        return loggerList.size();
    }

    /**
     * 设置日志等级
     * @param className
     * @param level
     */
    public static void setLogLevel(String className, String level){
        LoggerContext loggerContext= (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger=loggerContext.getLogger(className);
        logger.setLevel(Level.toLevel(level));
    }

    /**
     * 当level为空时，拿父级的level
     * @param logger
     * @return
     */
    private static String parentLevel(Logger logger){
        if(logger.getLevel() == null){
            Class<?> c1 = logger.getClass();
            try {
                //通过反射方法获取内部私有属性
                Field field = c1.getDeclaredField("parent");
                field.setAccessible(true);// 此属性对外部可见
                Object parent = field.get(logger);
                if(parent != null && parent instanceof Logger){
                    return parentLevel((Logger)parent);
                }
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logger.getLevel().levelStr;
    }

    /**
     * 修改日志等级
     * @param level
     */
    public static void setAllLogLevel(String level){
        LoggerContext loggerContext= (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        for(Logger logger : loggerList){
            logger.setLevel(Level.toLevel(level));
        }
    }
}