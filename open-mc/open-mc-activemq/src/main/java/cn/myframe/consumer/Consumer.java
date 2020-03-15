package cn.myframe.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2019/4/15/015 16:54
 * @Version 1.0
 */
@Component
public class Consumer {
    //监听队列，queue类型
    @JmsListener(destination="springboot.queue")
    public void receiveQueue(String text){
        System.out.println(this.getClass().getName()+ "收到的报文为:"+text);
    }

    //监听队列，topic类型，这里containerFactory要配置为jmsTopicListenerContainerFactory
    @JmsListener(destination = "springboot.topic",
            containerFactory = "jmsTopicListenerContainerFactory"
    )
    public void receiveTopic(String text) {
        System.out.println(this.getClass().getName()+" 收到的报文为:"+text);
    }


    @JmsListener(destination="springboot.queuereply")
    @SendTo("out.replyTo.queue") //消费者应答后通知生产者
    public String receiveQueueReply(String text){
        System.out.println(this.getClass().getName()+ "收到的报文为:"+text);
        return "out.replyTo.queue receiveQueueReply";
    }
}
