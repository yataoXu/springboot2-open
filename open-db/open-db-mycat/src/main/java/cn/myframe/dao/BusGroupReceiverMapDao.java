package cn.myframe.dao;

import cn.myframe.entity.BusGroupReceiverMapEntity;
import cn.myframe.entity.BusReceiverEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusGroupReceiverMapDao {

    @Insert("/*!mycat: catlet=io.mycat.route.sequence.BatchInsertSequence */INSERT INTO bus_group_receiver_map(receiver_id,group_id) VALUES(#{receiverId},#{groupId})")
    void insert(@Param("groupId") Long groupId,@Param("receiverId") Long receiverId);

    @Delete("delete from bus_group_receiver_map")
    void deleteAll();

    void batchInsert(List<BusGroupReceiverMapEntity> list);
}
