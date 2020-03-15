package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@EnableCaching
public class JpaApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(JpaApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
