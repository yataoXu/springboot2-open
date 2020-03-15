
package cn.myframe.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class PartitionListener {
    private static final String TPOIC = "topic2";

    @KafkaListener(id = "id0", containerFactory="batchFactory",
            topicPartitions = { @TopicPartition(topic = TPOIC, partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records) {
        log.info("Id0 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id0 records size " +  records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p0 Received message={}",  message);
            }
        }
    }

    @KafkaListener(id = "id1", topicPartitions = { @TopicPartition(topic = TPOIC, partitions = { "1" }) },containerFactory="batchFactory")
    public void listenPartition1(List<ConsumerRecord<?, ?>> records) {
        log.info("Id1 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id1 records size " +  records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p1 Received message={}",  message);
            }
        }
    }

    @KafkaListener(id = "id2", topicPartitions = { @TopicPartition(topic = TPOIC, partitions = { "2" }) },containerFactory="batchFactory")
    public void listenPartition2(List<ConsumerRecord<?, ?>> records) {
        log.info("Id2 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id2 records size " +  records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p2 Received message={}",  message);
            }
        }
    }

    @KafkaListener(id = "id3", topicPartitions = { @TopicPartition(topic = TPOIC, partitions = { "3" }) },containerFactory="batchFactory")
    public void listenPartition3(List<ConsumerRecord<?, ?>> records) {
        log.info("Id3 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id3 records size " + records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p3 Received message={}", message);
            }
        }
    }
}
