package cn.test;

import cn.myframe.config.EnableMyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author: ynz
 * @Date: 2019/4/16/016 17:31
 * @Version 1.0
 */
@SpringBootApplication
@EnableMyConfig
public class TestApplication {

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(TestApplication.class);
        // app.setWebEnvironment(false);
        ConfigurableApplicationContext context =app.run(args);

        String test = context.getBean("test",String.class);
        System.out.println("bean是否存在--->"+test);
    }
}
