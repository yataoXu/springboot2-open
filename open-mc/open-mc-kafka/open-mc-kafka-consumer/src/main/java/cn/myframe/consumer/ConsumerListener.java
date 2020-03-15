package cn.myframe.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author: ynz
 * @Date: 2018/12/29/029 14:02
 * @Version 1.0
 */
@Component
@Slf4j
public class ConsumerListener {
    //单条消息
      @KafkaListener(topics = {"first_top2"})
      public void consumer(ConsumerRecord<?, ?> record){
          Optional<?> kafkaMessage = Optional.ofNullable(record.value());
          if (kafkaMessage.isPresent()) {
              Object message = kafkaMessage.get();
              log.info("record =" + record);
              log.info(" message =" + message);
          }
      }

      //批量消息
    @KafkaListener(topics = {"first_top"},containerFactory="batchFactory")
    public void consumerBatch(List<ConsumerRecord<?, ?>> record){
        log.info("接收到消息数量：{}",record.size());
    }

    @KafkaListener(id = "group3", topics = "first_top3")
    public void annoListener(@Payload String data,
                             @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_TIMESTAMP) String ts) {
        log.info(" receive : \n"+
                "data : "+data+"\n"+
                "key : "+key+"\n"+
                "partitionId : "+partition+"\n"+
                "topic : "+topic+"\n"+
                "timestamp : "+ts+"\n"
        );
    }

    @KafkaListener(id = "ack", topics = "ack",containerFactory = "ackContainerFactory")
    public void ackListener(ConsumerRecord record, Acknowledgment ack) {
        log.info("receive : " + record.value());
        //手动提交
        ack.acknowledge();
    }



}
