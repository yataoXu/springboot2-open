package cn.myframe.repository;

import cn.myframe.entity.BusReceiverEntity;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReceiverRepository extends ElasticsearchRepository<BusReceiverEntity,Long> {

    /**
     * 关键字     方法命名
     * And          findByNameAndPwd
     * Or           findByNameOrSex
     * Is           findById
     * Between      findByIdBetween
     * Like         findByNameLike
     * NotLike      findByNameNotLike
     * OrderBy      findByIdOrderByXDesc
     * Not          findByNameNot
     * @param name
     * @param address
     * @return
     */
    List<BusReceiverEntity> findByNameAndAddress(String name, String address);

    List<BusReceiverEntity> findByName(String name);

    @Query("select * from BusReceiverEntity where name=?1")
    List<BusReceiverEntity> findBySql(String name);

}

