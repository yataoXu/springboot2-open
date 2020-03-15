package cn.myframe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/6/27/027 9:16
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "sys_role")
public class SysRoleEntity {

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(nullable = false, length =  200)
    private String roleName;

    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinTable(name="sys_role_permission_map",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="perm_id")})
    List<SysPermissionEntity> permissionEntityList;

}
