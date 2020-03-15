package cn.myframe.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ynz
 * @Date: 2019/1/11/011 11:19
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisObject implements java.io.Serializable {
    private static final long serialVersionUID = -6849794470754688719L;

    private String name;
}
