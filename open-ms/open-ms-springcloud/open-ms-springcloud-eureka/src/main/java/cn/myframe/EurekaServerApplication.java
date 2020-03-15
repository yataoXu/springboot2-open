package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * http://www.ityouknow.com/springcloud/2017/05/12/eureka-provider-constomer.html
 * 作为node1服务中心的配置，并将serviceUrl指向node2
 *
 * vm options:-Dserver.port=8001    Environment Variables: spring.profiles.active=server1
 *
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	
	public static void main(String[] args){
	    System.setProperty("spring.profiles.active","server1");
        SpringApplication app = new SpringApplication(EurekaServerApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
