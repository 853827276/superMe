package com.hengzhang.springboot.exception;

/**
 * 登陆异常
 * @author zhangh
 * @date 2018年9月6日下午12:02:32
 */
public class LoginException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 6082095423374066311L;

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(String message) {
		super(message);
	}
	
}
