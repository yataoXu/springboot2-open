package cn.myframe.service;

import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 13:05
 * @Version 1.0
 */
@Component("user2Service")
public class RemoteUserService4Impl implements RemoteUserService{
    @Override
    public String sayHello(String name) {
        return "第二种方法实现";
    }

    @Override
    public String delaySayHello(Long delayTime) {
        return null;
    }

    @Override
    public String attachment() {
        return null;
    }
}
