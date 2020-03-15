package cn.myframe.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*@WebListener
@Configuration*/
@Slf4j
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("自定义监听器");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
