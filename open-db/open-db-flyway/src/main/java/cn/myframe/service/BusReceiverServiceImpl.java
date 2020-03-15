package cn.myframe.service;

import cn.myframe.dao.BusReceiverDao;
import cn.myframe.entity.BusReceiverEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 8:51
 */
@Service
public class BusReceiverServiceImpl implements MybatisService<BusReceiverEntity> {

    @Resource
    BusReceiverDao receiverDao;

    @Resource
    BusReceiverServiceOther other;

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void insert(BusReceiverEntity receiverEntity) {
        receiverDao.insert(receiverEntity);
        other.innerBean(receiverEntity);
      //  throw new NullPointerException();
    }

    @Transactional(propagation = Propagation.NESTED)
    public void outer(){
        inner();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void inner(){

    }


}
