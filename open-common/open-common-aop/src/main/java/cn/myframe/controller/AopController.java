package cn.myframe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/25/025 17:43
 * @Version 1.0
 */
@RestController
public class AopController {

    @RequestMapping("/aop")
    public String aop(){
        return "suceess";
    }
}
