package cn.myframe;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.util.NameBuildUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public class TestMybatis {

    @Resource
    BusReceiverDao busReceiverDao;

   /* @Test*/
    public void insertReceiver(){
        BusReceiverEntity entity = NameBuildUtils.buildReceiver();
        System.out.println(entity.getCreateDate());
        System.out.println(JSON.toJSONString(entity));
        busReceiverDao.insert(entity);
    }

   /* @Test*/
    public void delete(){
        busReceiverDao.delete(1);
    }

   /* @Test*/
    public void batchInsert(){
        Long t = System.currentTimeMillis();
        List<BusReceiverEntity> list = new ArrayList<>();
        for(int i = 0;i<100000;i++){
            list.add(NameBuildUtils.buildReceiver());
            if(i%10000 == 0 && i != 0){
                busReceiverDao.batchInsert(list);
                list.clear();
            }
        }
        System.out.println(System.currentTimeMillis()-t);
    }
}
