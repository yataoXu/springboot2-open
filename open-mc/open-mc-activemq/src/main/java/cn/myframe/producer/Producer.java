package cn.myframe.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @Author: ynz
 * @Date: 2019/4/15/015 16:58
 * @Version 1.0
 */
@Component
public class Producer {

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    @Autowired
    private Queue queuereply;

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage(Destination destination, final String message){
        jmsTemplate.convertAndSend(destination, message);
    }

    public void sendQueueMessage(final String message){
        sendMessage(queue, message);
    }

    public void sendTopicMessage(final String message){
        sendMessage(topic, message);
    }

    public void sendQueueMessageReply(final String message){
        sendMessage(queuereply, message);
    }

    //生产者监听消费者的应答
    @JmsListener(destination = "out.replyTo.queue")
    public void consumerMessage(String text){
        System.out.println("从out.replyTo.queue收到报文"+text);
    }

}
