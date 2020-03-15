package cn.myframe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @Author: ynz
 * @Date: 2019/6/27/027 8:48
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUserEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false,unique = true, length =  50)
    private String userName;

    @Column(nullable = false, length =  200)
    private String password;

    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinTable(name="sys_user_role_map",joinColumns={@JoinColumn(name="user_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
    List<SysRoleEntity> roleList;
}
