package cn.myframe.entity;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 *
 */
@Data
@Entity
@Table(name = "bus_receiver")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BusReceiverEntity {

    //主键
    @Id
    @Column(name="id")
    private Long id;

    //姓名
    @Column(name="name",length = 32)
    private String name;
    //区域
    @Column(name="region_code")
    private String regionCode;
    //地址
    @Column(name="address")
    private String address;
    //地址英文名字
    @Column(name="enname")
    private String enName;
    //家庭成员
    @Column(name="memberfamily")
    private int memberFamily;
    //创建时间
    @Column(name="create_date")
    private Date createDate;


}
