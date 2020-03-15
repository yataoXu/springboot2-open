package cn.myframe.service.generic;

/**
 * @Author: ynz
 * @Date: 2019/1/3/003 13:32
 * @Version 1.0
 */
public interface ActionService<T> {
    T query();
    String save(T t);
}
