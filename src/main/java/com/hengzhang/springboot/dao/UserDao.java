package com.hengzhang.springboot.dao;

import org.apache.ibatis.annotations.Param;

import com.hengzhang.springboot.common.BaseDao;
import com.hengzhang.springboot.entity.User;
public interface UserDao extends BaseDao {
	public User login(@Param("name") String name,@Param("password") String password);
	public void updatePwd(@Param("uid") String uid,@Param("password") String password);
	public User checkOldPwd(@Param("uid") String uid, @Param("password") String oldPwd);
}