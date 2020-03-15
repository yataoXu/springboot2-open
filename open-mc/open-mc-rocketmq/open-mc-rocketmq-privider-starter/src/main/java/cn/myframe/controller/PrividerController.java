package cn.myframe.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: ynz
 * @Date: 2019/1/26/026 10:13
 * @Version 1.0
 */
@RestController
@Slf4j
public class PrividerController {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/send/{topic}/{msg}")
    public String send(@PathVariable String topic,@PathVariable String msg){
        rocketMQTemplate.convertAndSend(topic+":tag1", msg+":tag1");

        for (int i = 0; i < 1; i++) {
            rocketMQTemplate.asyncSendOrderly(topic, msg+i,String.valueOf(1),new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("传输成功");
                    log.info(JSON.toJSONString(sendResult));
                }
                @Override
                public void onException(Throwable e) {
                    log.error("传输失败", e);
                }
            });
        }


       // rocketMQTemplate.


        return "send";
    }

    @RequestMapping("/sendtx/{topic}/{msg}")
    public String sendTx(@PathVariable String topic,@PathVariable String msg) throws InterruptedException {
        Message message =  MessageBuilder.withPayload(msg).build();
        System.out.println(Thread.currentThread().getName());

        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction("group1", topic, message,"test");

        System.out.println(result.getTransactionId());

        return "sendtx";
    }



}
