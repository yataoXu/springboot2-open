package cn.myframe.support;

import cn.myframe.dao.SysUserDao;
import cn.myframe.entity.SysPermissionEntity;
import cn.myframe.entity.SysRoleEntity;
import cn.myframe.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserDao sysUserDao;

    /**
     * 授权的时候是对角色授权，而认证的时候应该基于资源，而不是角色，因为资源是不变的，而用户的角色是会变的
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity userEntity = sysUserDao.findByUserName(username);
        if (null == userEntity) {
            throw new UsernameNotFoundException(username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<SysRoleEntity> roleList = userEntity.getRoleList();
        if(roleList == null || roleList.size() == 0){
            return new User(userEntity.getUserName(), userEntity.getPassword(), authorities);
        }
        for (SysRoleEntity role : roleList) {
            List<SysPermissionEntity> permList = role.getPermissionEntityList();
            if(permList == null){
                continue;
            }
            for(SysPermissionEntity permission : permList){
                //添加用户的权限
                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
            }
        }
        return new User(userEntity.getUserName(), userEntity.getPassword(), authorities);
    }
}
