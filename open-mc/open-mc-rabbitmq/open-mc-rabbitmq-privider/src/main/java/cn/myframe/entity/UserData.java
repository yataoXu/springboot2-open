package cn.myframe.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ynz
 * @Date: 2018/12/28/028 8:49
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class UserData implements Serializable {

    private String userName;
    private int age;
    private String work;

    public UserData(String userName){
        this.userName = userName;
        this.age =2;
    }
}
