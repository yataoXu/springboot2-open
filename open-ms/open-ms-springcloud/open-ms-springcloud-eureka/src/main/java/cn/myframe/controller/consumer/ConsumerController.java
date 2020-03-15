package cn.myframe.controller.consumer;

import cn.myframe.service.consumer.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: ynz
 * @Date: 2019/1/31/031 11:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Autowired
    ProducerService producerService;

    @RequestMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
        return producerService.hello(name);
    }
}
