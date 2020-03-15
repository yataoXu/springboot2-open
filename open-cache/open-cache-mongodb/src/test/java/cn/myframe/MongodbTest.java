package cn.myframe;

import cn.myframe.dao.BusReceiverDaoImp;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.util.NameBuildUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public class MongodbTest {

    @Autowired
    private BusReceiverDaoImp busReceiverDao;

    /**
     * 新增一条数据
     */
    /*@Test*/
    public void save(){
        busReceiverDao.insert(NameBuildUtils.buildReceiver());
    }

    /**
     * 批量插入
     */
   /* @Test*/
    public void batchInsert(){
        List<BusReceiverEntity> list = new ArrayList<BusReceiverEntity>();
        for(int i = 0; i<1000;i++){
            list.add(NameBuildUtils.buildReceiver());
        }
        busReceiverDao.batchInsert(list);
    }

    /**
     * 按ID查找用户
     */
    /*@Test*/
    public void queryById(){
        BusReceiverEntity entity = busReceiverDao.findById(60000001L);
        System.out.println(JSON.toJSONString(entity));
    }

    /**
     * 删除数据
     */
   /* @Test*/
    public void delete(){
        busReceiverDao.delete(60000001L);
    }
}
