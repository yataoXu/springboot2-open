package cn.myframe.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @Author: ynz
 * @Date: 2019/4/15/015 16:52
 * @Version 1.0
 */
@Configuration
@EnableJms //启用jms功能
public class ActiveMqConfig {

    //如果要使用topic类型的消息，则需要配置该bean
    @Bean("jmsTopicListenerContainerFactory")
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(
            ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory
                = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true); //这里必须设置为true，false则表示是queue类型
        return factory;
    }


    @Bean
    public Queue queue() {
        return new ActiveMQQueue("springboot.queue") ;
    }

    @Bean
    public Topic topic() {
        return new ActiveMQTopic("springboot.topic") ;
    }

    @Bean("queuereply")
    public Queue queuereply() {
        return new ActiveMQQueue("springboot.queuereply") ;
    }



}
