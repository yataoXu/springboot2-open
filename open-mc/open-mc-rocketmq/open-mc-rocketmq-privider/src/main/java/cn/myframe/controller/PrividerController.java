package cn.myframe.controller;

import cn.myframe.listener.TxTransactionListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/1/26/026 10:13
 * @Version 1.0
 */
@RestController
@Slf4j
public class PrividerController {

    @Autowired
    private DefaultMQProducer defaultProducer;

    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @Autowired
    TxTransactionListener txTransactionListener;

    @GetMapping("/test")
    public void test(String info) throws Exception {
        Message message = new Message("TopicTest", "Tag1", "12345", "rocketmq测试成功".getBytes());
        // 这里用到了这个mq的异步处理，类似ajax，可以得到发送到mq的情况，并做相应的处理
        //不过要注意的是这个是异步的
        defaultProducer.send(message, new SendCallback() {
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

    @GetMapping("/tx")
    public void tx(String info) throws Exception {
        Message message = new Message("TopicTest", "Tag1", "12345", "tx测试成功".getBytes());
        // 这里用到了这个mq的异步处理，类似ajax，可以得到发送到mq的情况，并做相应的处理
        //不过要注意的是这个是异步的
        transactionMQProducer.setTransactionListener(txTransactionListener);
        transactionMQProducer.sendMessageInTransaction(message, new SendCallback() {
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


}
