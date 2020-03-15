package cn.myframe;

import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.service.BusReceiverService;
import cn.myframe.util.NameBuildUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public class TestReadWrite {

    @Resource
    BusReceiverService busReceiverService;

   /* @Test*/
    public void test(){
        busReceiverService.insertReceiver(NameBuildUtils.buildReceiver());
        BusReceiverEntity entity = busReceiverService.findById(11L);
        System.out.println(JSON.toJSONString(entity));
    }


}
