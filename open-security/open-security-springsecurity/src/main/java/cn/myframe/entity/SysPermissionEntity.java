package cn.myframe.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: ynz
 * @Date: 2019/6/28/028 8:58
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "sys_permission")
public class SysPermissionEntity {

    @Id
    @Column(name = "perm_id")
    private Long permId;

    @Column(length = 30)
    private String code;
}
