package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@EnableAsync
public class AsynApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(AsynApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
