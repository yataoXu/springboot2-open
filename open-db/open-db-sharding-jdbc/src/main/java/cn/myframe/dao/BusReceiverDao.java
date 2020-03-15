package cn.myframe.dao;

import cn.myframe.entity.BusReceiverEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.io.Serializable;
import java.util.List;

public interface BusReceiverDao {

    BusReceiverEntity findById(Serializable id);

    @Insert("INSERT INTO bus_receiver(name,region_code,address,enname,memberfamily) VALUES(#{name}, #{regionCode}, #{address}, #{enName}, #{memberFamily})")
    void insert(BusReceiverEntity receiver);

    @Delete("DELETE FROM bus_receiver WHERE id =#{id}")
    void delete(Serializable id);

    void batchInsert(List<BusReceiverEntity> list);
}
