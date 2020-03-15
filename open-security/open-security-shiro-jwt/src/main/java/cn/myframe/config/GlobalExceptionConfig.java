package cn.myframe.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: ynz
 * @Date: 2018/12/26/026 15:08
 * @Version 1.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionConfig {
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public String handlerException(Exception e) {
        return "你没有权限操作";
    }
}
