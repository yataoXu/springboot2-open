package cn.myframe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/6/28/028 10:28
 * @Version 1.0
 */
@RestController("/level1")
public class LevelController {

    @RequestMapping("/login")
    public String login(){
        return "success";
    }
}
