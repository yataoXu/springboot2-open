package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
public class RabbitConsumerApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(RabbitConsumerApplication.class);
        System.setProperty("spring.devtools.restart.enabled", "false");
        app.run(args);
    }

}
