package com.hengzhang.springboot.exception;

/**
 * 参数异常
 * @author weiliu36
 * @date 2018-07-19下午2:35:51
 */
public class ArgumentsException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public ArgumentsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArgumentsException(String message) {
		super(message);
	}
	
}
