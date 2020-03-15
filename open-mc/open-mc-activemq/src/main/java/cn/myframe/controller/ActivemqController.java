package cn.myframe.controller;

import cn.myframe.producer.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

/**
 * @Author: ynz
 * @Date: 2019/4/15/015 17:05
 * @Version 1.0
 */
@RestController
public class ActivemqController{

    @Autowired
    Producer producer;

    @RequestMapping("/send/{msg}")
    public String  send(@PathVariable String msg){
        Destination destination = new ActiveMQQueue("springboot.queuereply");
        producer.sendMessage(destination, msg);

        //按队列发送消息
        producer.sendQueueMessage("queue:"+msg);
        //按topic发送消息
        producer.sendTopicMessage("topic:"+msg);
        //发送应答消息
        producer.sendQueueMessageReply("reply:"+msg);

        return msg;
    }


}
