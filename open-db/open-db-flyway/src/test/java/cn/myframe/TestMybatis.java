package cn.myframe;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.service.BusReceiverServiceImpl;
import cn.myframe.util.NameBuildUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMybatis {

    @Resource
    BusReceiverDao busReceiverDao;

    @Resource
    BusReceiverServiceImpl busReceiverService;

    @Test
    public void insertReceiver(){
        BusReceiverEntity entity = NameBuildUtils.buildReceiver();
        entity.setId(null);
        System.out.println(entity.getCreateDate());
        System.out.println(JSON.toJSONString(entity));
        busReceiverService.insert(entity);
    }


}
