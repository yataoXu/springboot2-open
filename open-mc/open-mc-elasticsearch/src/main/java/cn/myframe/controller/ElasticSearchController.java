package cn.myframe.controller;

import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.service.BusReceiverService;
import cn.myframe.utils.NameBuildUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/1/17/017 11:32
 * @Version 1.0
 */
@RestController
public class ElasticSearchController {

    @Autowired
    BusReceiverService busReceiverService;

    @RequestMapping("/save")
    public String save(){
        busReceiverService.save(NameBuildUtils.buildReceiver());
        return "save";
    }

    @RequestMapping("/batchSave/{count}")
    public String save(@PathVariable  int count){
        Long startTime = System.currentTimeMillis();
        List<BusReceiverEntity> list = new ArrayList<BusReceiverEntity>();
        for(int i=1; i<=count; i++){
            if(i%10000 == 0){
                list.add(NameBuildUtils.buildReceiver());
                busReceiverService.saveBatch(list);
                list.clear();
            }else{
                list.add(NameBuildUtils.buildReceiver());
            }
        }
        if(list.size()>0){
            busReceiverService.saveBatch(list);
        }
        return "batchSave:"+(System.currentTimeMillis()-startTime);
    }

    @RequestMapping("/deleteAll")
    public String deleteAll(){
        busReceiverService.deleteAll();
        return "deleteAll";
    }
}
