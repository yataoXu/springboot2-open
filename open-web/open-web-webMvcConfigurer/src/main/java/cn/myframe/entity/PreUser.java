package cn.myframe.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "myframe.user")
@Data
public class PreUser {
    private String name;
    private int age;
    private String address;
    private String phone;
    private String desc;
    private Date date;
    private List<String> hobby;
}