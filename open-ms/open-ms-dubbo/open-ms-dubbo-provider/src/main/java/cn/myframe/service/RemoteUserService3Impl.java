package cn.myframe.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 13:05
 * @Version 1.0
 */
@Component("userService")
public class RemoteUserService3Impl implements RemoteUserService{
    @Override
    public String sayHello(String name) {
        return "第三种方法实现";
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
