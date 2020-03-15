package cn.myframe.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: ynz
 * @Date: 2018/12/28/028 12:30
 * @Version 1.0
 */
@Configuration
public class ConsumerRabbitmqConfig {

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);             //开启手动 ack
        //开启的信道数量
        ExecutorService service=Executors.newFixedThreadPool(600);
        factory.setTaskExecutor(service);
        factory.setConcurrentConsumers(500);
        /**
         * 每个消费者获取最大投递数量 默认250
         */
        factory.setPrefetchCount(500);
        return factory;
    }

}
