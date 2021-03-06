package cn.myframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@MapperScan("cn.myframe.dao")
@ServletComponentScan
public class MybatisApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(MybatisApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
