package cn.myframe.controller;

import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.service.BusReceiverService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/6/25/025 14:32
 * @Version 1.0
 */
@RestController
public class JpaController {

    @Autowired
    BusReceiverService busReceiverService;

    @RequestMapping("findName/{name}")
    public String findByName(@PathVariable String name){
        BusReceiverEntity busReceiverEntity = busReceiverService.findByName(name);
        return JSON.toJSONString(busReceiverEntity);
    }
}
