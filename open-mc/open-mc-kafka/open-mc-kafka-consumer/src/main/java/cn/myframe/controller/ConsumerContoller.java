package cn.myframe.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: ynz
 * @Date: 2018/12/30/030 19:07
 */
@Slf4j
@RestController
public class ConsumerContoller {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private ConsumerFactory consumerFactory;

    @GetMapping("/stop")
    public String stop(){
        registry.getListenerContainer("forward").pause();
        return "success";
    }

    @GetMapping("/start")
    public String start(){
       //判断监听容器是否启动，未启动则将其启动
        if (!registry.getListenerContainer("forward").isRunning()) {
            registry.getListenerContainer("forward").start();
        }
        registry.getListenerContainer("forward").resume();
        return "success";
    }

}
