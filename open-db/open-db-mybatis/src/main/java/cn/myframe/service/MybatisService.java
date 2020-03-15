package cn.myframe.service;

import java.io.Serializable;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 8:51
 */
public interface MybatisService<T> {

    T findById(Serializable id);

}
