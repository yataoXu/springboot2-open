package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * spring.profiles.active=producer2
 * -Dserver.port=9003
 *
 * @EnableDiscoveryClient注解后，项目就具有了服务注册的功能
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Producer2Application {

    public static void main(String[] args){
        System.setProperty("spring.profiles.active","producer2");
        SpringApplication app = new SpringApplication(Producer2Application.class);
        // app.setWebEnvironment(false);
        app.run(args);
    }
}
