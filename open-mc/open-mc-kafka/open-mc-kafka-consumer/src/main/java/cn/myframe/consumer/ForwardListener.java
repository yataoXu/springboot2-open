package cn.myframe.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2018/12/30/030 15:13
 */
@Slf4j
@Component
public class ForwardListener {

    @KafkaListener(id = "forward", topics = "first_top4")
    @SendTo("first_top2")
    public String forward(String data) {
        log.info("接收到消息数量：{}",data);
        return "send msg : " + data;
    }
}
