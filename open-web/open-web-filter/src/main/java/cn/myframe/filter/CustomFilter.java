package cn.myframe.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//注册器名称为customFilter,拦截的url为所有
@WebFilter(filterName="customFilter",urlPatterns={"/*"},
        initParams = {
                @WebInitParam(name = "excludeUrl", value = "/exclude")
        })
@Slf4j
@Order(5)
public class CustomFilter implements Filter {
    private String exclusions = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        exclusions = filterConfig.getInitParameter("excludeUrl");
        //项目启动时初始化,只始化一次
        log.info("filter 初始化,excludeUrl:"+exclusions);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        log.info(request.getRequestURI());
        if (request.getRequestURI().equals(exclusions)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        log.info("CustomFilter 请求处理");
        log.info("请求地址："+request.getRequestURL());
        //对request、response进行一些预处理
        // 比如设置请求编码
        // servletRequest.setCharacterEncoding("UTF-8");
        // servletResponse.setCharacterEncoding("UTF-8");
        //链路 直接传给下一个过滤器
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("filter 销毁");
    }
}
