package cn.myframe;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Bean;

/**
 * 
 * 启动方法
 *
 * Hystrix具备拥有回退机制和断路器功能的线程和信号隔离，请求缓存和请求打包，以及监控和配置等功能
 *
 * HystrixDashboard:
 * 访问地址：http://10.10.2.60:8093/hystrix
 * http://10.10.2.60:8093/hystrix.stream
 *
 *
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@EnableDubboConfiguration
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
/*@EnableTurbine*/
public class DubboHystrixApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(DubboHystrixApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

}
