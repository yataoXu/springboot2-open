package cn.myframe.controller;

import cn.myframe.service.RemoteUserService;
import cn.myframe.service.callback.CallbackListener;
import cn.myframe.service.callback.CallbackService;
import cn.myframe.service.eventnotity.IDemoService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.remoting.exchange.ResponseFuture;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * dubbo消费者controller
 * @since 2018-07-03 18:44
 **/
@RestController
public class RemoteUserController implements Serializable{

    //timeout 可以不指定，但是version一定要指定 不然会找不到服务 直连需要加url="dubbo://localhost:20880"
    //启动时检查提供者是否存在，true报错，false忽略
    //@Reference(version = "2.7.0",loadbalance="consistenthash",cache="lru")
    //@Reference(version = "2.7.0",async=true,sent=false)
    @Reference(version = "2.7.0",async = true,sent = true,sticky = true)
    private RemoteUserService remoteUser;

  /*  @Reference(version = "2.7.0",interfaceClass = ActionService.class,generic=true)
    private ActionService actionService;*/

   /* @Reference(version = "2.7.0")
    private RemoteUserService remoteUser2;*/

   @Reference(version = "2.6.0")
   private CallbackService callbackService;

   @Autowired
   private IDemoService demoService;


   public String mockError(){
       return "error";
   }


    @RequestMapping(value="/dubbo/notity/{name}")
    public String notity(@PathVariable("name") String name){
        demoService.get(name);
        return "success";
    }


    @RequestMapping(value="/dubbo/sayhello/{name}")
    public String sayHello(@PathVariable("name") String name){
        String result=remoteUser.sayHello(name);
        return result;
    }

   /* @RequestMapping(value="/dubbo/save")
    public String save(){
        String result = actionService.save(new User("uername",1));
        return result;
    }*/

   @RequestMapping(value="/dubbo/listener/{name}")
   public void listener(@PathVariable("name") String name){
       callbackService.addListener(name,new CallbackListener(){
           public void changed(String msg) {
               System.out.println("callback1:" + msg);
           }
       });
   }

    @RequestMapping(value="/dubbo/delayHello/{time}")
    public String delayHello(@PathVariable long time){
        String result = remoteUser.delaySayHello(time);
        return result;
    }

    @RequestMapping(value="/dubbo/grayscaleHello/{name}")
    public String grayscaleHello(@PathVariable("name") String name){
        String result=remoteUser.sayHello(name);
        return result;
    }

    /**
     * 设置隐式参数
     * @return
     */
    @RequestMapping(value="/dubbo/attachment/{name}")
    public String attachment(@PathVariable("name") String name){
        RpcContext.getContext().setAttachment("name", "1");
        String result=remoteUser.attachment();
        return result;
    }

    @RequestMapping(value="/dubbo/delay2/{time}")
    public String delay2(@PathVariable long time){
        remoteUser.delaySayHello(time);
        // 拿到Dubbo内置的ResponseFuture并设置回调
        ResponseFuture responseFuture = ((FutureAdapter) RpcContext.getContext().getFuture()).getFuture();
        responseFuture.setCallback(new ResponseCallback() {
            @Override
            public void done(Object result) {
                System.out.println("异步回调返回结果:" + result);
            }
            @Override
            public void caught(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return "success";
    }










}
