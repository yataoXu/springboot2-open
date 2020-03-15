package cn.myframe.dao;

import cn.myframe.entity.BusReceiverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @Author: ynz
 * @Date: 2019/6/25/025 14:22
 * @Version 1.0
 */
@Repository
public interface BusReceiverDao  extends JpaRepository<BusReceiverEntity,Long> {


    BusReceiverEntity findByName(String name);

    BusReceiverEntity findByNameAndAddress(String name,String address);

    @Transactional
    @Query(value = "update BusReceiverEntity p set p.name = ?1")
    @Modifying
    Integer updateName(String name);

/*    @Query("SELECT r FROM BusReceiverEntity r where r.name = ?1")
   // @Modifying
    BusReceiverEntity findByName(String name);*/

}
