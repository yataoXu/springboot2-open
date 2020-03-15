package cn.myframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
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
@MapperScan("cn.myframe.dao")
@ServletComponentScan
/*@ImportResource("classpath:transaction.xml")*/
public class TxApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(TxApplication.class);
       // app.setAllowBeanDefinitionOverriding(true);
        app.run(args);
    }


}
