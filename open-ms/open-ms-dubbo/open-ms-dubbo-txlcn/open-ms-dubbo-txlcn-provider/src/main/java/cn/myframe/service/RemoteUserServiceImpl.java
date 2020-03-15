package cn.myframe.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.commons.annotation.TccTransaction;
import com.codingapi.txlcn.commons.annotation.TxTransaction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Service(version = "2.7.0",timeout = 10000,interfaceClass = RemoteUserService.class)
public class RemoteUserServiceImpl implements  RemoteUserService {

    @Value("${server.port}")
    private Integer port;

    @Override
    @TccTransaction(confirmMethod = "cm", cancelMethod = "cl", executeClass = RemoteUserServiceImpl.class)
    public String sayHello(String name) {
        System.out.println("provider");
       /* try {
            Thread.sleep(20_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //throw new IllegalArgumentException();
        return "hi!server port:"+port+";name="+name+";time:"+System.currentTimeMillis();
    }

    public void cm(String name) {
        log.info("tcc-confirm-" + DTXLocal.getOrNew().getGroupId());
     //   ids.remove(DTXLocal.getOrNew().getGroupId());
    }

    public void cl(String name) {
        log.info("tcc-cancel-" + DTXLocal.getOrNew().getGroupId());
       // demoMapper.deleteById(ids.get(DTXLocal.getOrNew().getGroupId()));
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
