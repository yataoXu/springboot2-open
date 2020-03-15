package cn.myframe.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author: ynz
 * @Date: 2018/12/30/030 20:02
 */
@Slf4j
@Component
public class FilterListener {

    @KafkaListener(topics = {"filter_topic"},containerFactory="filterContainerFactory")
    public void consumerBatch(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("record =" + record);
            log.info("接收到消息数量：{}",message);
        }

    }
}
