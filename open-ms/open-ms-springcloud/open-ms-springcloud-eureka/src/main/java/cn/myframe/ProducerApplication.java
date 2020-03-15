package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @EnableDiscoveryClient注解后，项目就具有了服务注册的功能
 *
 * vm options:-Dserver.port=9001    Environment Variables: spring.profiles.active=producer
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProducerApplication {

    public static void main(String[] args){
        System.setProperty("spring.profiles.active","producer");
        SpringApplication app = new SpringApplication(ProducerApplication.class);
        // app.setWebEnvironment(false);
        app.run(args);
    }
}
