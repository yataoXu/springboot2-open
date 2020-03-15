package cn.myframe.service.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: ynz
 * @Date: 2019/1/31/031 11:27
 * @Version 1.0
 */
@FeignClient(name= "spring-cloud-producer")
public interface ProducerService {

    @RequestMapping(value = "/producer/hello")
    public String hello(@RequestParam(value = "name") String name);

}
