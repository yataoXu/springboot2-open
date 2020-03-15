package cn.myframe.config;

import org.springframework.context.annotation.Bean;

/**
 * @Author: ynz
 * @Date: 2019/4/16/016 17:58
 * @Version 1.0
 */
public class MyConfiguration2 {
    @Bean
    public String test2() {
        return "自定义配置类2";
    }
}
