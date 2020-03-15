package cn.myframe.service.callback;


import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ynz
 * @Date: 2019/1/3/003 15:16
 * @Version 1.0
 */
@Component("callbackService")
public class CallbackServiceImpl implements CallbackService{
    public final static Map<String, CallbackListener> listeners = new ConcurrentHashMap<String, CallbackListener>();

    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        listener.changed("key:"+key+",调用回调方法"); // 发送变更通知
    }

}
