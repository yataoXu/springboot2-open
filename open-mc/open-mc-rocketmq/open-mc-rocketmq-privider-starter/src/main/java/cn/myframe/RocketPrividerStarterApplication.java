package cn.myframe;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;

/**
 *
 * https://github.com/apache/rocketmq-spring/blob/master/README_zh_CN.md
 *
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
public class RocketPrividerStarterApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(RocketPrividerStarterApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
