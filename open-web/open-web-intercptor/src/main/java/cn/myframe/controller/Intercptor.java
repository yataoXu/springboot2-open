package cn.myframe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/25/025 8:20
 * @Version 1.0
 */
@RestController
public class Intercptor {

    @RequestMapping("/intercptor")
    public String intercptor(){
        return "success";
    }
}
