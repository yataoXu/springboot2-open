package cn.myframe.service;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BusReceiverService {

    @Resource
    BusReceiverDao busReceiverDao;

    public void insertReceiver(BusReceiverEntity entity){
        busReceiverDao.insert(entity);
    }

    public BusReceiverEntity findById(Long id){
        return busReceiverDao.findById(id);
    }

}
