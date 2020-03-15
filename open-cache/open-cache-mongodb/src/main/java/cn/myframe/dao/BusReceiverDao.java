package cn.myframe.dao;

import java.io.Serializable;
import java.util.List;

interface BaseDao<T> {

    void insert(T entity);

    void batchInsert(List<T> list);

    T findById(Serializable id);

    void update(T entity);

    void delete(Serializable id);
}
