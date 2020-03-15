package cn.myframe.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.commons.annotation.TccTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2019/1/24/024 11:37
 * @Version 1.0
 */
@Component
@Slf4j
public class ExecutorServiceImp implements ExecutorService{
    @Reference(version = "2.7.0")
    private RemoteUserService remoteUser;

    @TccTransaction(confirmMethod = "cm", cancelMethod = "cl", executeClass = ExecutorServiceImp.class)
    public String executor(String name){
        log.info("executor-"+ DTXLocal.transactionState());
       // log.info("executor-"+ DTXLocal.getOrNew().getUserTransactionState());
       // String result=remoteUser.sayHello(name);
        String result=remoteUser.sayHello(name);
        log.info("executor-"+ DTXLocal.transactionState());
        try {
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException();
       // return result;
    }

    public void cm(String name) {
        log.info("tcc-confirm-" + DTXLocal.getOrNew().getGroupId());

    }

    public void cl(String name) {
        log.info("tcc-cancel-" + DTXLocal.getOrNew().getGroupId());
        log.info("tcc-cancel-"+ DTXLocal.getOrNew().getSysTransactionState());
        log.info("tcc-cancel-"+ DTXLocal.getOrNew().getUserTransactionState());
    }
}
