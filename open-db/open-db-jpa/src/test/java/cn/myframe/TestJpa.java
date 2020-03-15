package cn.myframe;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.service.BusReceiverService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

/**
 * @Author: ynz
 * @Date: 2019/6/25/025 14:36
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJpa {

    @Autowired
    BusReceiverService busReceiverService;

    @Autowired
    BusReceiverDao busReceiverDao;


   /*@Test*/
    public void findByName(){
        BusReceiverEntity busReceiverEntity = busReceiverDao.findByName("test");
        System.out.println(JSON.toJSONString(busReceiverEntity));
        busReceiverEntity = busReceiverDao.findByName("test");
        System.out.println(JSON.toJSONString(busReceiverEntity));
    }

    @Test
    public void updateName(){
        BusReceiverEntity busReceiverEntity = busReceiverDao.findById(1L).get();
        System.out.println(JSON.toJSONString(busReceiverEntity));
        busReceiverEntity = busReceiverDao.findById(1L).get();
        System.out.println(JSON.toJSONString(busReceiverEntity));

        //busReceiverDao.updateName("test");
    }

    /**
     * 查询全部列表,并做分页
     *  @param pageNum 开始页数
     * @param pageSize 每页显示的数据条数
     */
    public void selectAll(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<BusReceiverEntity> users = busReceiverDao.findAll(pageable);
        Iterator<BusReceiverEntity> userIterator =  users.iterator();
    }
}
