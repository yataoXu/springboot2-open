package cn.myframe.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ynz
 * @Date: 2019/1/3/003 13:46
 * @Version 1.0
 */
@AllArgsConstructor
@Data
public class Project implements Serializable {

    private String projectName;
}
