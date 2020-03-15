package cn.myframe.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/6/4/004 11:08
 * @Version 1.0
 */
@RestController
public class SentinelController {

    @RequestMapping("/hello")
    @SentinelResource("hello")
    public String hello(){
        return "sayHello";
    }
}
