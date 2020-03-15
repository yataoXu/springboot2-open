package cn.myframe.service.impl;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.service.BusReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ynz
 * @Date: 2019/6/25/025 14:24
 * @Version 1.0
 */
@Service
public class BusReceiverServiceImpl implements BusReceiverService {

    @Autowired
    BusReceiverDao busReceiverDao;

    @Override
    public BusReceiverEntity findByName(String name) {
        return busReceiverDao.findByName(name);
    }
}
