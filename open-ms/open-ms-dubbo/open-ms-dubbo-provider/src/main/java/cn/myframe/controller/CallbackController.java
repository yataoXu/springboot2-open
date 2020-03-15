package cn.myframe.controller;

import cn.myframe.service.callback.CallbackListener;
import cn.myframe.service.callback.CallbackServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 14:10
 * @Version 1.0
 */
@RestController
public class CallbackController {

    @GetMapping("/listener")
    public String listener(){
        for(Map.Entry<String, CallbackListener> entry : CallbackServiceImpl.listeners.entrySet()) {
            entry.getValue().changed("key:" + entry.getKey() + ",调用回调方法");

        }
        return "success";
    }
}
