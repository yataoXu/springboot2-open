/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cn.myframe.dao;

import cn.myframe.entity.SysUserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户
 */
@Component
public class SysUserDao  {

	private static List<SysUserEntity> userList = new ArrayList<>();

	static{
		userList.add(new SysUserEntity(1L,"tom","123456","sys:read",""));
		userList.add(new SysUserEntity(2L,"jam","123456","sys:delete",""));
	}

	//@Select("select * from sys_user where username = #{username}")
	public SysUserEntity selectOne(String userName){
		for(SysUserEntity entity : userList){
			if(entity.getUsername().equals(userName)){
				return entity;
			}
		}
		return null;
	};

	//@Select("select permission from sys_user where user_id = #{userId}")
	public String queryAllPerms(Long userId){
		for(SysUserEntity entity : userList){
			if(entity.getUserId().longValue() == userId.longValue()){
				return entity.getPermission();
			}
		}
		return null;
	};
}
