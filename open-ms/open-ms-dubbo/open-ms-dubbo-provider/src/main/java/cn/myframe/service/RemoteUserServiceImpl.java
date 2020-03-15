package cn.myframe.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//@Service(version = "2.7.0",group = "service1",timeout = 10000,interfaceClass = RemoteUserService.class)
public class RemoteUserServiceImpl implements  RemoteUserService {

    @Value("${server.port}")
    private Integer port;

    @Override
    public String sayHello(String name) {
        System.out.println("provider");
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hi!server port:"+port+";name="+name+";time:"+System.currentTimeMillis();
    }

    @Override
    public String delaySayHello(Long delayTime){
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hi!server port:"+port+";delayTime:"+delayTime;
    }

    @Override
    public String attachment() {
        String name = RpcContext.getContext().getAttachment("name");
        return "name:" + name;
    }
}
