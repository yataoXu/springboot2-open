package cn.myframe.controller;

import cn.myframe.service.ExecutorServiceImp;
import com.codingapi.txlcn.client.bean.DTXLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * dubbo消费者controller
 * @since 2018-07-03 18:44
 **/
@RestController
public class RemoteUserController implements Serializable{


    @Autowired
    ExecutorServiceImp executorService;



    @RequestMapping(value="/dubbo/sayhello/{name}")
   // @TxTransaction(type = "txc")
    public String sayHello(@PathVariable("name") String name){
        return executorService.executor(name);
    }



}
