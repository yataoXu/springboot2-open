package cn.myframe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/25/025 8:38
 * @Version 1.0
 */
@RestController
public class FilterController {

    @RequestMapping("/filter")
    public String filter(){
        return "success";
    }

    @RequestMapping("/exclude")
    public String exclude(){
        return "success";
    }
}
