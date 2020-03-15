package cn.myframe.service;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author: ynz
 * @Date: 2019/6/3/003 11:24
 * @Version 1.0
 */
@Service
public class BusReceiverServiceOther{

    @Resource
    BusReceiverDao receiverDao;

    @Transactional(propagation =  Propagation.NOT_SUPPORTED)
    public void innerBean(BusReceiverEntity receiverEntity){
        receiverEntity.setName("11111111");
        receiverDao.insert(receiverEntity);
        throw new NullPointerException();
    }
}
