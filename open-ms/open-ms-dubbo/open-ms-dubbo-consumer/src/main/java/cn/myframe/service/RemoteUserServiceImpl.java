package cn.myframe.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Component
/*@Service(version = "2.7.0",timeout = 10000,interfaceClass = RemoteUserService.class)*/
public class RemoteUserServiceImpl implements  RemoteUserService {
    @Override
    public String sayHello(String name) {
        System.out.println("comsumer");
        return "hello!"+name;
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
