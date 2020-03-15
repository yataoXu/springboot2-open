package cn.myframe.service;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 8:51
 */
@Service
public class BusReceiverServiceImpl implements MybatisService<BusReceiverEntity> {

    @Resource
    BusReceiverDao receiverDao;

    @Override
    public  BusReceiverEntity findById(Serializable id) {
       // receiverDao.findById(id);
        return receiverDao.findById(id);
    }

    public List<BusReceiverEntity> findList(String name,String address){
        return receiverDao.findList(name, address);
    }
}
