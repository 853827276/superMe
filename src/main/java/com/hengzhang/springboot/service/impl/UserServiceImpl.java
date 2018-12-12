package com.hengzhang.springboot.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengzhang.springboot.dao.UserDao;
import com.hengzhang.springboot.entity.User;
import com.hengzhang.springboot.service.UserService;
import com.hengzhang.springboot.util.MyEnum;
import com.hengzhang.springboot.util.RequestContextHolderUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired UserDao userDao;
	
	@Override
	public Map<String, Object> login(String name, String password) throws Exception {
		Map<String, Object> map = new HashMap<>();
		User user = userDao.login(name, password) ;
		if(user== null){
			throw new Exception("用户名或者密码错误，请重新输入。");
		}
		//写入cookie
		RequestContextHolderUtil.setCookieValue(MyEnum.USER_COOK_KEY.getMsg(), user.getId(), null);
		if(MyEnum.USER_FIRST_LOGIN.getMsg().equals(user.getFirstLogin())){//第一次登陆
			map.put("firstLogin", "1");
			userDao.updateStatus(user.getId(), "1");
		}else{
			map.put("firstLogin", "0");
		}
		return map;
	}

	@Override
	public void updatePwd(String oldPwd, String newPwd) {
		String uid = RequestContextHolderUtil.getCookieValue(MyEnum.USER_COOK_KEY.getMsg());
		if(null != userDao.checkOldPwd(uid, oldPwd)){
			userDao.updatePwd(uid, newPwd);
			RequestContextHolderUtil.clearValue(MyEnum.USER_COOK_KEY.getMsg());
		}
	}

	@Override
	public void logout() {
		RequestContextHolderUtil.clearValue(MyEnum.USER_COOK_KEY.getMsg());
	}

	@Override
	public void checkOldPwd(String oldPwd) throws Exception {
		String uid = RequestContextHolderUtil.getCookieValue(MyEnum.USER_COOK_KEY.getMsg());
		if(null == userDao.checkOldPwd(uid, oldPwd)){
			throw new Exception("输入的原密码错误");
		}
	}

}
