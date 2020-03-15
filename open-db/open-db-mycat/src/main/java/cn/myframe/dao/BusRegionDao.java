package cn.myframe.dao;

import cn.myframe.entity.BusGroupEntity;
import cn.myframe.entity.BusRegionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

public interface BusRegionDao {

    @Select("select * from bus_region where id = #{id}")
    BusRegionEntity findById(@Param("id") Serializable id);

    @Insert("INSERT INTO bus_region(id,region_name,region_code) VALUES(#{id},#{regionName}, #{regionCode})")
    void insert(BusRegionEntity region);

    @Delete("delete from bus_region")
    void deleteAll();
}
