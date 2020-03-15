package cn.myframe;

import cn.myframe.service.RemoteUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DubboConsumerTest {

    @Reference(version = "2.6.0")
    private RemoteUserService remoteUserService;

    @Test
    public void say(){

        String result=remoteUserService.sayHello("tom");
        System.out.println(result);

    }

}
