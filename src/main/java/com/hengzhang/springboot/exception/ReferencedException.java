package com.hengzhang.springboot.exception;

/**
 * 对象被引用异常
 * @author weiliu36
 * @date 2018-07-19下午2:35:51
 */
public class ReferencedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReferencedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReferencedException(String message) {
		super(message);
	}

}
