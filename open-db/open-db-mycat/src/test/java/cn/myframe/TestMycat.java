package cn.myframe;

import cn.myframe.dao.BusGroupDao;
import cn.myframe.dao.BusGroupReceiverMapDao;
import cn.myframe.dao.BusReceiverDao;
import cn.myframe.dao.BusRegionDao;
import cn.myframe.entity.BusGroupEntity;
import cn.myframe.entity.BusGroupReceiverMapEntity;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.entity.BusRegionEntity;
import cn.myframe.util.NameBuildUtils;
import cn.myframe.util.SnowflakeIdWorker;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public class TestMycat {

    @Resource
    BusReceiverDao busReceiverDao;

    @Resource
    BusGroupReceiverMapDao busGroupReceiverMapDao;

    @Resource
    BusGroupDao busGroupDao;

    @Resource
    BusRegionDao busRegionDao;

   /* @Test*/
    public void insertReceiver(){
        BusReceiverEntity entity = NameBuildUtils.buildReceiver();
        System.out.println(entity.getCreateDate());
        System.out.println(JSON.toJSONString(entity));
        busReceiverDao.insertSequence(entity);
        BusReceiverEntity entity2 = busReceiverDao.findById(15L);
        System.out.println(JSON.toJSONString(entity2));
    }

   /* @Test*/
    public void delete(){
        busReceiverDao.delete(1);
    }

    @Test
    public void batchInsert(){
        Long t = System.currentTimeMillis();
        List<BusReceiverEntity> list = new ArrayList<>();
        List<BusGroupReceiverMapEntity> mapList =  new ArrayList<>();
        for(int i = 0;i<2000000;i++){
            BusReceiverEntity entity = NameBuildUtils.buildReceiver();
            Long id = SnowflakeIdWorker.buildId();
            entity.setId(id);
            list.add(NameBuildUtils.buildReceiver());
            long groupId = new Random().nextInt(100);
          //  mapList.add(new BusGroupReceiverMapEntity(null,groupId,id));
            if(i%10000 == 0 && i != 0){
                busReceiverDao.batchInsert(list);
              //  busGroupReceiverMapDao.batchInsert(mapList);
                list.clear();
                mapList.clear();
            }
        }
        System.out.println(System.currentTimeMillis()-t);
    }

    @Test
    public void init(){
        for(int i = 0 ;i<10;i++){
            BusReceiverEntity entity = NameBuildUtils.buildReceiver();
            Long id = SnowflakeIdWorker.buildId();
            entity.setId(id);
            long groupId = new Random().nextInt(100);
            //  mapList.add(new BusGroupReceiverMapEntity(null,groupId,id));
            busReceiverDao.insert(entity);
            busGroupReceiverMapDao.insert(groupId,id);
        }


    }

    @Test
    public void queryPage(){
        List<BusReceiverEntity> list = busReceiverDao.queryPage(1,10);
        list.forEach(entity -> {
            System.out.println(JSON.toJSONString(entity));
        });


    }

    /*@Test*/
    public void erInsert(){
        for(int i = 0;i<10;i++){
            BusReceiverEntity entity = NameBuildUtils.buildReceiver();
            Long id = SnowflakeIdWorker.buildId();
            entity.setId(id);
            busReceiverDao.insert(entity);
            long l = new Random().nextInt(99);
            busGroupReceiverMapDao.insert(l,id);
        }

    }

    @Test
    public void deleteAll(){
        busReceiverDao.deleteAll();
        busGroupReceiverMapDao.deleteAll();
        busGroupDao.deleteAll();
        busRegionDao.deleteAll();
    }

    @Test
    public void insertGroup(){
        for(long i = 0 ; i<100; i++){
            busGroupDao.insert(new BusGroupEntity(i,"name"+i,"code"+i,new Date()));
        }
    }

    @Test
    public void insertRegion(){
        for(long i = 0 ; i<2001; i++){
            busRegionDao.insert(new BusRegionEntity(i,"regionName"+i,"regionCode"+i));
        }
    }

}
