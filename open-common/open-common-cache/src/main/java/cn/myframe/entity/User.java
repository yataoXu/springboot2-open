package cn.myframe.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class User {
    @Value("${myframe.user.name}")
    private String name;
    @Value("${myframe.user.age}")
    private int age;
    @Value("${myframe.user.address}")
    private String address;
    @Value("${myframe.user.desc}")
    private String desc;
    @Value("${myframe.user.uuid}")
    private String uuId;
    @Value("${myframe.user.phone:13422337766}")
    private String phone;
}
