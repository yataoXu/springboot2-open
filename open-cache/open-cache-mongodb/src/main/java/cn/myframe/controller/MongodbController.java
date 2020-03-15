package cn.myframe.controller;

import cn.myframe.dao.BusReceiverDaoImp;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.utils.NameBuildUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/1/14/014 8:10
 * @Version 1.0
 */
@RestController
public class MongodbController {

    @Autowired
    private BusReceiverDaoImp busReceiverDao;

    @GetMapping("/save")
    public String save(){
        busReceiverDao.insert(NameBuildUtils.buildReceiver());
        return  "save";
    }

    /**
     * 批量插入
     *
     * @return
     */
    @GetMapping("/batchInsert")
    public String batchInsert(){
        List<BusReceiverEntity> list = new ArrayList<BusReceiverEntity>();
        for(int j = 0;j<30000;j++){
            for(int i = 0; i<1000;i++){
                list.add(NameBuildUtils.buildReceiver());
            }
            busReceiverDao.batchInsert(list);
            list.clear();
        }
        return "String";
    }

    @GetMapping("/findPage/{pageNo}/{pageSize}")
    public List<BusReceiverEntity> findPage(@PathVariable int pageNo,@PathVariable int pageSize){
        long startTime = System.currentTimeMillis();
        List<BusReceiverEntity>  list =  busReceiverDao.findPage(pageSize,pageNo);
        System.out.println(JSON.toJSONString(list));
        System.out.println(System.currentTimeMillis()-startTime);
        long startTime2 = System.currentTimeMillis();
        List<BusReceiverEntity>  list2 =  busReceiverDao.findPage2(pageSize,pageNo,80000990);
        System.out.println(JSON.toJSONString(list2));
        System.out.println(System.currentTimeMillis()-startTime2);
        return list2;
    }

    /**
     * 按ID查找用户
     */
    @GetMapping("/queryById/{id}")
    public String queryById(@PathVariable String id){
        BusReceiverEntity entity = busReceiverDao.findById(60000001L);
        return JSON.toJSONString(entity);
    }

    /**
     * 删除数据
     */
    @GetMapping("/delete/{id}")
    public String deleteBy(@PathVariable String id){
        busReceiverDao.delete(60000001L);
        return "delete";
    }
}
