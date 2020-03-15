package cn.myframe.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/5/7/007 17:37
 * @Version 1.0
 */
public class MyInterceptor implements Interceptor,Interceptor.Builder {
    private static final Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
    //拦截字符串
    private String filterStr;
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        try {
            String body = new String(event.getBody(), "UTF-8");
            logger.info("filter:{}",body);
            if(body!=null){
                //过滤以a开始的字符串
                if(body.contains(filterStr)){
                    logger.info("{}被过滤........",body);
                    return null;
                }
                body = body + ":intercept";
                return EventBuilder.withBody(body.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        logger.info("filter:{}","list intercept");
        return null;
    }

    @Override
    public void close() {
    }

    @Override
    public Interceptor build() {
        return this;
    }

    @Override
    public void configure(Context context) {
        filterStr = context.getString("filter_str","a");
    }

}
