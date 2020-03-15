package cn.myframe;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@ImportResource(value = {"classpath:dubbo.xml"})
@EnableDubbo
public class DubboProviderXMLApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(DubboProviderXMLApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
     }
}
