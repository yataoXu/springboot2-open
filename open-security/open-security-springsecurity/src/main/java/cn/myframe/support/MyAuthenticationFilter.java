package cn.myframe.support;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: ynz
 * @Date: 2019/6/28/028 10:58
 * @Version 1.0
 */
public class MyAuthenticationFilter  extends AbstractAuthenticationProcessingFilter {

    public MyAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String userName = (String) req.getSession().getAttribute("username");
        if("test".equals(userName)){
            super.unsuccessfulAuthentication(req, res,new InsufficientAuthenticationException("输入的验证码不正确"));
        }
       // super.unsuccessfulAuthentication(req, res,new InsufficientAuthenticationException("输入的验证码不正确"));
        chain.doFilter(request, response);
    }
}
