package cn.myframe.dao;

import cn.myframe.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: ynz
 * @Date: 2019/6/27/027 9:21
 * @Version 1.0
 */
@Repository
public interface SysUserDao  extends JpaRepository<SysUserEntity, Long> {

     SysUserEntity findByUserName(String userName);
}
