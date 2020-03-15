package cn.myframe.controller.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/1/31/031 11:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Value("${server.port}")
    private int port;

    @RequestMapping("/hello")
    public String index(@RequestParam String name) {
        return "hello "+name+"，this is first messge,port:"+port;
    }
}
