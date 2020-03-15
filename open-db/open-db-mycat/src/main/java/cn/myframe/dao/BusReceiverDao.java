package cn.myframe.dao;

import cn.myframe.entity.BusReceiverEntity;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.List;

public interface BusReceiverDao {

    BusReceiverEntity findById(Serializable id);

    @Insert("INSERT INTO bus_receiver(id,name,region_code,address,enname,memberfamily) VALUES(#{id},#{name}, #{regionCode}, #{address}, #{enName}, #{memberFamily})")
    void insert(BusReceiverEntity receiver);

    @Insert("/*!mycat: catlet=io.mycat.route.sequence.BatchInsertSequence */INSERT INTO bus_receiver(name,region_code,address,enname,memberfamily) VALUES(#{name}, #{regionCode}, #{address}, #{enName}, #{memberFamily})")
    void insertSequence(BusReceiverEntity receiver);

    @Delete("DELETE FROM bus_receiver WHERE id =#{id}")
    void delete(Serializable id);

    /**
     *
     * @param list
     */
    void batchInsert(List<BusReceiverEntity> list);

    /**
     * 批量插入与ID自增长结合的支持
     * @param list
     */
    void batchInsertSequence(List<BusReceiverEntity> list);

    @Select(" select * from bus_receiver ORDER BY id limit #{pageNo},#{count} ")
    @Results({
                @Result(property ="busRegion",column="region_code"
                ,one =@One(select ="cn.myframe.dao.BusRegionDao.findById")),
                @Result(property ="regionCode",column = "region_code"),
                @Result(property ="id",column = "id"),
                @Result(property = "groupList", column = "id",
                        many = @Many(select = "cn.myframe.dao.BusGroupDao.findByReceiver"))
                }
            )
    List<BusReceiverEntity> queryPage(@Param("pageNo")int pageNo, @Param("count")int count);

    @Delete("delete from bus_receiver")
    void deleteAll();
}
