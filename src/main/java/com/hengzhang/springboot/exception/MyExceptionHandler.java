package com.hengzhang.springboot.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import com.hengzhang.springboot.util.ResultWrapper;



/**
 * 全局异常处理机制
 * @author zhangh
 * @date 2018年7月18日上午10:35:33
 */
@ControllerAdvice
public class MyExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyExceptionHandler.class);
	
	/**
	 * 全局异常处理
	 * 
	 * @param e
	 * @return 
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<?, ?> handle(Exception e) {
		return ResultWrapper.wrapErr(e.getMessage());
	}

	@ExceptionHandler(value = ArgumentsException.class)
	@ResponseBody
	public Map<?, ?> handleArgumentsException(Exception e) {
		LOGGER.error("参数异常："+e.getMessage());
		return ResultWrapper.wrapErr("参数异常："+e.getMessage());
	}
	
	@ExceptionHandler(value = MultipartException.class)
	@ResponseBody
	public Map<?, ?> handleMultipartException(Exception e) {
		LOGGER.error("文件上传异常：上传文件不能大于100Mb");
		return ResultWrapper.wrapErr("文件上传异常：上传文件不能大于100Mb");
	}

	@ExceptionHandler(value = LoginException.class)
	@ResponseBody
	public Map<?, ?> handleLoginException(Exception e) {
		LOGGER.error("登陆异常："+e.getMessage());
		return ResultWrapper.wrapLogin("登陆异常："+e.getMessage());
	}
}