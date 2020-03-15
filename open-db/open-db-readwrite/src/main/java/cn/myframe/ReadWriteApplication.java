package cn.myframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@MapperScan("cn.myframe.dao")
public class ReadWriteApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(ReadWriteApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
