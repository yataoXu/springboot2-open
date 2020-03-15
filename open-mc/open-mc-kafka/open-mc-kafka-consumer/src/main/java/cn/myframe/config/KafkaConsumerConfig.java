package cn.myframe.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

/**
 * @Author: ynz
 * @Date: 2018/12/29/029 14:18
 * @Version 1.0
 */
@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Bean
    public KafkaListenerContainerFactory<?> batchFactory(ConsumerFactory consumerFactory){
        ConcurrentKafkaListenerContainerFactory<Integer,String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(10);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.setBatchListener(true);//设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
      //  factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);//设置提交偏移量的方式
        return factory;
    }

    /**
     * 设置手动提交
     * @param consumerFactory
     * @return
     */
    @Bean("ackContainerFactory")
    public ConcurrentKafkaListenerContainerFactory ackContainerFactory(
            ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    /**
     * 消息过滤
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory(ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        //配合RecordFilterStrategy使用，被过滤的信息将被丢弃
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(new RecordFilterStrategy() {
            @Override
            public boolean filter(ConsumerRecord consumerRecord) {
               String msg = (String) consumerRecord.value();
               if(msg.contains("abc")){
                   return false;
               }
                log.info("filterContainerFactory filter : "+msg);
                //返回true将会被丢弃
                return true;
            }
        });
        return factory;
    }





    @Bean
    public KafkaMessageListenerContainer demoListenerContainer(ConsumerFactory consumerFactory) {
        ContainerProperties properties = new ContainerProperties("topic3");
        properties.setGroupId("group1");
      /*
      //批量消费
      properties.setMessageListener(new MessageListener<Integer,String>() {
            @Override
            public void onMessage(ConsumerRecord<Integer, String> record) {
                log.info("topic3: " + record.toString());
            }
        });*/

        //批量消费
        properties.setMessageListener(
                new BatchAcknowledgingConsumerAwareMessageListener<String,String>(){
                    @Override
                    public void onMessage(List<ConsumerRecord<String, String>> list,
                                          Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
                        log.info("size:{}",list.size());
                    }
        });
        return new KafkaMessageListenerContainer(consumerFactory, properties);
    }

}
