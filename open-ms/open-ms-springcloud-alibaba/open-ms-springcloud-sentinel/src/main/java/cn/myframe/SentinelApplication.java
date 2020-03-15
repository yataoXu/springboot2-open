package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * vm options:-Dserver.port=9002    Environment Variables: spring.profiles.active=consumer
 */
@SpringBootApplication
public class SentinelApplication {

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(SentinelApplication.class);
        app.run(args);
    }
}
