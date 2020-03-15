package cn.myframe.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/5/6/006 8:26
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "comment")
@Data
public class CommentProperties {

    private String author;
    private List<String> cookieValues;
}
