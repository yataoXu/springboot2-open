package cn.myframe.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/24/024 17:21
 * @Version 1.0
 */
@RestController
@Slf4j
public class SayHelloController {


    @RequestMapping("/sayHello")
    public String user(){

        return "sayHello";
    }
}
