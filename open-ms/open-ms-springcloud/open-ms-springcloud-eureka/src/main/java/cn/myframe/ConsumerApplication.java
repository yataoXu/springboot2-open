package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * vm options:-Dserver.port=9002    Environment Variables: spring.profiles.active=consumer
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

    public static void main(String[] args){
        System.setProperty("spring.profiles.active","consumer");
        SpringApplication app = new SpringApplication(ConsumerApplication.class);
        // app.setWebEnvironment(false);
        app.run(args);
    }
}
