package cn.myframe.controller;

import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.service.BusReceiverServiceImpl;
import cn.myframe.service.MybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 8:49
 */
@RestController
public class MybatisController {

    @Autowired
    BusReceiverServiceImpl busReceiverServiceImpl;

    @RequestMapping("/receiver/findByid/{id}")
    public BusReceiverEntity findById(@PathVariable Long id){
        return busReceiverServiceImpl.findById(id);
    }

    @RequestMapping("/receiver/findList/{name}/{address}")
    public List<BusReceiverEntity> findList(@PathVariable String name,@PathVariable String address){
        return busReceiverServiceImpl.findList(name,address);
    }

}
