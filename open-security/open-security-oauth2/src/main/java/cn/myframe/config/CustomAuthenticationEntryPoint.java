package cn.myframe.config;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Token异常信息
 * 自定义401错误码内容
 * @Author: ynz
 * @Date: 2019/6/28/028 14:46
 * @Version 1.0
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("Pre-authenticated entry point called. Rejecting access");

        //返回错误页
       // httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Access Denied");

        //返回JSON信息
        Map map = new HashMap();
        map.put("error", "401");
        map.put("message",  e.getMessage());
        map.put("path", httpServletRequest.getServletPath());
        map.put("timestamp", String.valueOf(new Date().getTime()));
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(httpServletResponse.getOutputStream(), map);
        } catch (Exception e1) {
            throw new ServletException();
        }

    }
}
