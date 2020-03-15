package cn.myframe;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusGroupReceiverMapEntity;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.util.NameBuildUtils;
import cn.myframe.util.SnowflakeIdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public class TestTableShared {
    @Resource
    BusReceiverDao busReceiverDao;

    /**
     * 清空数据
     */
    @Test
    public void deleteAll(){
        busReceiverDao.deleteAll();
    }

    /**
     * 批量查找数据
     */
    @Test
    public void batchInsert(){
        Long t = System.currentTimeMillis();
        List<BusReceiverEntity> list = new ArrayList<>();
        for(int i = 1;i<=10000000;i++){
            BusReceiverEntity entity = NameBuildUtils.buildReceiver();
            Long id = SnowflakeIdWorker.buildId();
            entity.setId(id);
            list.add(NameBuildUtils.buildReceiver());
            if(i%10000 == 0 ){
                Long t1 = System.currentTimeMillis();
                busReceiverDao.batchInsert(list);
                System.out.println(System.currentTimeMillis()-t1);
                list.clear();
            }
        }
        System.out.println(System.currentTimeMillis()-t);
    }
}
