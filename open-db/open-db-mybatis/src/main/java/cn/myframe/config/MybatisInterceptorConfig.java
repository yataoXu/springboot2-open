package cn.myframe.config;

import cn.myframe.interceptor.ExecutorInterceptor;
import cn.myframe.interceptor.ParamInterceptor;
import cn.myframe.interceptor.ResultInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 15:05
 */
@Configuration
public class MybatisInterceptorConfig {
    @Bean
    public String myInterceptor(SqlSessionFactory sqlSessionFactory) {
        ExecutorInterceptor executorInterceptor = new ExecutorInterceptor();
        Properties properties = new Properties();
        properties.setProperty("prop1","value1");
        executorInterceptor.setProperties(properties);
        sqlSessionFactory.getConfiguration().addInterceptor(executorInterceptor);
        sqlSessionFactory.getConfiguration().addInterceptor(new ParamInterceptor());
        sqlSessionFactory.getConfiguration().addInterceptor(new ResultInterceptor());
        return "interceptor";
    }
}
