package cn.myframe.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: ynz
 * @Date: 2019/1/31/031 13:45
 * @Version 1.0
 */
@Component
public class ProducerFallBackService implements ProducerService{
    public String hello(String name){
        return "fallBack:"+name;
    }
}
