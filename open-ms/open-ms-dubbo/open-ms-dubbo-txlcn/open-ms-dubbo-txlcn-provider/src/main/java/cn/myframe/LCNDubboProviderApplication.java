package cn.myframe;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.codingapi.txlcn.client.config.EnableDistributedTransaction;
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
@EnableDubboConfiguration
@EnableDistributedTransaction
public class LCNDubboProviderApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(LCNDubboProviderApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
