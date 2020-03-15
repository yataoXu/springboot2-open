package cn.myframe.service;

import cn.myframe.entity.BusReceiverEntity;

/**
 * @Author: ynz
 * @Date: 2019/6/25/025 14:23
 * @Version 1.0
 */
public interface BusReceiverService {

    BusReceiverEntity findByName(String name);
}
