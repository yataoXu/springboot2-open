package cn.myframe.controller;

import cn.myframe.config.KafkaSendResultHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * @Author: ynz
 * @Date: 2018/12/29/029 13:55
 * @Version 1.0
 */
@RestController
@Slf4j
public class KafkaPrividerController {

    @Autowired
    private KafkaTemplate template;

    @Autowired
    private KafkaSendResultHandler producerListener;

    @ApiOperation(value = "三种方式发消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic", value = "topic", defaultValue = "first_top", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "msg", value = "发送的消息", defaultValue = "message",  required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/send")
    public String send(@RequestParam String topic, @RequestParam String msg){
       // template.setProducerListener(producerListener);
        //发送带有时间戳的消息
        template.send(topic, 0, System.currentTimeMillis(), "0", msg);

        //使用ProducerRecord发送消息
        ProducerRecord record = new ProducerRecord(topic, msg);
        template.send(record);

        //使用Message发送消息
        Map map = new HashMap();
        map.put(KafkaHeaders.TOPIC, topic);
        map.put(KafkaHeaders.PARTITION_ID, 0);
        map.put(KafkaHeaders.MESSAGE_KEY, "0");
        GenericMessage message = new GenericMessage(msg,new MessageHeaders(map));
        template.send(message);
        return "success";
    }

    @ApiOperation(value = "批量发消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic", value = "topic", defaultValue = "first_top", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "count", value = "发送多少遍", defaultValue = "10", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "msg", value = "发送的消息", defaultValue = "message",  required = true, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/batchSendMsg")
    public String batchSendMsg(@RequestParam String topic, @RequestParam int count, @RequestParam String msg) {
        for (int i=0; i < count; i++) {
            template.send(topic, msg);
        }
        return "send success";
    }

    @ApiOperation(value = "消息结果回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic", value = "topic", defaultValue = "first_top", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "msg", value = "发送的消息", defaultValue = "message",  required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/producerListen")
    public String producerListen(@RequestParam String topic, @RequestParam String msg) throws InterruptedException {
        template.send(topic, msg);
        return "success";
    }

    @ApiOperation(value = "发送同步消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic", value = "topic", defaultValue = "first_top", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "msg", value = "发送的消息", defaultValue = "message",  required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/syncMsg")
    public String syncMsg(@RequestParam String topic, @RequestParam String msg){
        try {
            template.send(topic, msg).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "success";
    }



}
