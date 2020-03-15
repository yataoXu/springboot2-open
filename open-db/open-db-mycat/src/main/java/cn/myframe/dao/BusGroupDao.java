package cn.myframe.dao;

import cn.myframe.entity.BusGroupEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

public interface BusGroupDao {

    @Insert("INSERT INTO bus_group(id,group_name,group_code,create_date) VALUES(#{id},#{groupName}, #{groupCode}, #{createDate})")
    void insert(BusGroupEntity group);

    @Delete("delete from bus_group")
    void deleteAll();

    /**
     * bus_group 为全局表，bus_receiver主表，bus_group_receiver_map为bus_receiver的子表
     * @param id
     * @return
     */
    @Select("SELECT g.* from bus_group g,bus_group_receiver_map m,bus_receiver r where  g.id =m.group_id and r.id = m.receiver_id and r.id=#{id}")
    List<BusGroupEntity> findByReceiver(@Param("id") Serializable id);

}
