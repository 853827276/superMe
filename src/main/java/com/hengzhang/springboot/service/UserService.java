package com.hengzhang.springboot.service;

import java.util.Map;

import com.hengzhang.springboot.common.BaseService;

/**
 * 用户接口
 * 
 * @author zhangh
 * @date 2018年9月6日上午10:18:28
 */
public interface UserService extends BaseService {
	/**
	 * 登陆
	 * 
	 * @author zhangh
	 * @date 2018年9月6日上午10:17:30
	 * @param name
	 * @param password
	 * @throws Exception 
	 */
	public Map<String, Object> login(String name, String password) throws Exception;

	/**
	 * 修改密码
	 * 
	 * @author zhangh
	 * @date 2018年9月6日上午10:17:38
	 * @param oldPwd
	 * @param newPwd
	 */
	public void updatePwd(String oldPwd, String newPwd);

	/**
	 * 退出
	 * 
	 * @author zhangh
	 * @date 2018年9月6日上午10:18:17
	 */
	public void logout();

	/**
	 * 验证原密码
	 * @author zhangh
	 * @date 2018年9月6日上午10:30:13
	 * @param oldPwd
	 * @throws Exception 
	 */
	public void checkOldPwd(String oldPwd) throws Exception;
}