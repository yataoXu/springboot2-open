package cn.myframe.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @Author: ynz
 * @Date: 2019/7/1/001 8:55
 * @Version 1.0
 */
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    public CustomOauthException(String msg) {
        super(msg);
    }

    public CustomOauthException(String msg, Throwable t) {
        super(msg, t);

    }

}
