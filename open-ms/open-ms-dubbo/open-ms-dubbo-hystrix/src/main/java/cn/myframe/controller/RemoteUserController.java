package cn.myframe.controller;

import cn.myframe.service.RemoteUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * dubbo消费者controller
 * @author DUCHONG
 * @since 2018-07-03 18:44
 **/
@RestController
public class RemoteUserController {

    //timeout 可以不指定，但是version一定要指定 不然会找不到服务 直连需要加url="dubbo://localhost:20880"
    //启动时检查提供者是否存在，true报错，false忽略
    @Reference(version = "2.6.0",check=false)
    private RemoteUserService remoteUser;

    //最大并发请求数，默认10，该参数当使用ExecutionIsolationStrategy.SEMAPHORE策略时才有效。
    // 如果达到最大并发请求数，请求会被拒绝。理论上选择semaphore size的原则和选择thread size一致，但选用semaphore时每次执行的单元要比较小且执行速度快（ms级别），否则的话应该用thread。
    //semaphore应该占整个容器（tomcat）的线程池的一小部分。

    // circuitBreaker.errorThresholdPercentage 错误比率阀值，如果错误率>=该值，circuit会被打开，并短路所有请求触发fallback。默认50
    //circuitBreaker.forceOpen 强制打开熔断器，如果打开这个开关，那么拒绝所有request，默认false
    //circuitBreaker.forceClosed 强制关闭熔断器 如果这个开关打开，circuit将一直关闭且忽略circuitBreaker.errorThresholdPercentage
    @RequestMapping(value="/dubbo/hystrix/{name}")
    @HystrixCommand(
            fallbackMethod = "reliable",
            threadPoolProperties = {  //10个核心线程池,超过20个的队列外的请求被拒绝; 当一切都是正常的时候，线程池一般仅会有1到2个线程激活来提供服务
                    @HystrixProperty(name = "coreSize", value = "10"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "20")},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //命令执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"), //一个rolling window内最小的请求数。如果设为20，那么当一个rolling window的时间内（比如说1个rolling window是10秒）收到19个请求，即使19个请求都失败，也不会触发circuit break。默认20
                    /*@HystrixProperty(name = "execution.isolation.strategy",value="SEMAPHORE"),*///隔离策略，默认是Thread, 可选THREAD｜SEMAPHORE
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000") //断路30s后尝试执行, 触发短路的时间值，当该值设为5000时，则当触发circuit break后的5000毫秒内都会拒绝request，也就是5000毫秒后才会关闭circuit。默认5000
            })
    public String hystrix(@PathVariable("name") String name){
        String result=remoteUser.sayhystrix(name);
        return result;
    }

    @RequestMapping(value="/dubbo/hystrix2/{name}")
    @HystrixCommand(
            fallbackMethod = "reliable",
            threadPoolProperties = {  //10个核心线程池,超过20个的队列外的请求被拒绝; 当一切都是正常的时候，线程池一般仅会有1到2个线程激活来提供服务
                    @HystrixProperty(name = "coreSize", value = "10"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "20")},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //命令执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"), //一个rolling window内最小的请求数。如果设为20，那么当一个rolling window的时间内（比如说1个rolling window是10秒）收到19个请求，即使19个请求都失败，也不会触发circuit break。默认20
                    /*@HystrixProperty(name = "execution.isolation.strategy",value="SEMAPHORE"),*///隔离策略，默认是Thread, 可选THREAD｜SEMAPHORE
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000") //断路30s后尝试执行, 触发短路的时间值，当该值设为5000时，则当触发circuit break后的5000毫秒内都会拒绝request，也就是5000毫秒后才会关闭circuit。默认5000
            })
    public String hystrix2(@PathVariable("name") String name){
        String result=remoteUser.sayhystrix(name);
        return result;
    }

    public String reliable(String name){
        return "error,hystrix!";
    }

}
