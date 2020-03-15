package cn.myframe.service;

import com.alibaba.dubbo.config.annotation.Reference;

public interface RemoteUserService {

    String sayHello(String name);

    String delaySayHello(Long delayTime);

    String attachment();
}
