package cn.myframe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ynz
 * @Date: 2019/4/16/016 17:16
 * @Version 1.0
 */
@Configuration
public class MyConfiguration {

    @Bean
    public String test() {
        return "自定义配置类";
    }
}
