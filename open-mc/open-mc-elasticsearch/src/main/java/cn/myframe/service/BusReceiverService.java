package cn.myframe.service;

import cn.myframe.entity.BusReceiverEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: ynz
 * @Date: 2019/1/17/017 11:25
 * @Version 1.0
 */
public interface BusReceiverService {

    List<BusReceiverEntity> queryPage();

    BusReceiverEntity queryObject(Long id);

    void save(BusReceiverEntity t);

    void saveBatch(List<BusReceiverEntity> list);

    List<BusReceiverEntity> queryList(Map<String, Object> map);

    void update(BusReceiverEntity t);

    void delete(Long id);

    void deleteAll();

    List<BusReceiverEntity> queryByName(String name);
}
