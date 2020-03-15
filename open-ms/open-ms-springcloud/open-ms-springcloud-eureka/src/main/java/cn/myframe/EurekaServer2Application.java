package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * vm options:-Dserver.port=8002    Environment Variables: spring.profiles.active=server2
 * 作为node1服务中心的配置，并将serviceUrl指向node2
 *
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer2Application {
	
	public static void main(String[] args){
        System.setProperty("spring.profiles.active","server2");
        SpringApplication app = new SpringApplication(EurekaServer2Application.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
