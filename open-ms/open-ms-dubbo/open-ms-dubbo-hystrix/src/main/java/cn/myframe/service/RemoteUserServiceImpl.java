package cn.myframe.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Component;

@Component
@Service(version = "2.6.0",timeout = 10000,interfaceClass = RemoteUserService.class)
public class RemoteUserServiceImpl implements  RemoteUserService {
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000") })


    @Override
    public String sayHello(String name) {
        System.out.println("provider");
        return "hi!"+name;
    }

    @Override
    public String sayhystrix(String name) {
        /*try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "hystrix!"+ name;
    }
}
