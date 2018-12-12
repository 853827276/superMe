package com.hengzhang.springboot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hengzhang.springboot.anno.CheckLogin;
import com.hengzhang.springboot.anno.CheckParam;
import com.hengzhang.springboot.anno.NotEmpty;
import com.hengzhang.springboot.common.BaseController;
import com.hengzhang.springboot.entity.User;
import com.hengzhang.springboot.service.UserService;
import com.hengzhang.springboot.util.ResultWrapper;

/**
 * 用户管理类
 * @author zhangh
 * @date 2018年9月6日下午2:51:03
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<User>{
	
	@Autowired UserService userService;
	
	/**
	 * 登陆接口
	 * @author zhangh
	 * @date 2018年9月6日上午10:20:28
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	@CheckParam
	@RequestMapping("/login")
	public Map<?, ?> login(@NotEmpty("用户名不能为空") String name,@NotEmpty("密码不能为空") String password) throws Exception{
		return ResultWrapper.wrapObjSuccess(userService.login(name, password),"登陆成功");
	}
	
	/**
	 * 退出
	 * @author zhangh
	 * @date 2018年9月6日上午10:22:19
	 * @return
	 */
	@RequestMapping("/logout")
	public Map<?, ?> logout( ){
		userService.logout();
		return ResultWrapper.wrapSuccess("成功退出");
	}
	
	/**
	 * 密码修改接口
	 * @author zhangh
	 * @date 2018年9月6日上午10:23:17
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@CheckLogin
	@CheckParam
	@RequestMapping("/updatePwd")
	public Map<?, ?> updatePwd(@NotEmpty("原密码不能为空") String oldPwd,@NotEmpty("新密码不能为空") String newPwd){
		userService.updatePwd(oldPwd, newPwd);
		return ResultWrapper.wrapSuccess("密码修改成功");
	}
	
	/**
	 * 验证原密码
	 * @author zhangh
	 * @date 2018年9月6日上午10:34:11
	 * @param oldPwd
	 * @return
	 * @throws Exception
	 */
	@CheckLogin
	@CheckParam
	@RequestMapping("/checkOldPwd")
	public Map<?, ?> checkOldPwd(@NotEmpty("原密码不能为空") String oldPwd) throws Exception{
		userService.checkOldPwd(oldPwd);
		return ResultWrapper.wrapSuccess("密码修改成功");
	}
}