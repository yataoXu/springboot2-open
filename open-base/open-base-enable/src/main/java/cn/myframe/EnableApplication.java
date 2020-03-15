package cn.myframe;

import cn.myframe.config.EnableMyConfig;
import cn.myframe.config.EnableMyConfig2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableMyConfig2
public class EnableApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(EnableApplication.class);
       // app.setWebEnvironment(false);
        ConfigurableApplicationContext context =app.run(args);

        String test = context.getBean("test2",String.class);
        System.out.println("bean是否存在--->"+test);
    }

}
