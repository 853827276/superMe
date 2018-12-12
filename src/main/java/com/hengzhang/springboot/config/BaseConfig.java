package com.hengzhang.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hengzhang.springboot.aspecj.ControllerMethodAdvice;
import com.hengzhang.springboot.aspecj.DaoMethodHandle;
import com.hengzhang.springboot.aspecj.ServiceMethodAdvice;
import com.hengzhang.springboot.util.SpringUtil;

/**
 * Aop 拦截机制，该框架会自动处理数据链
 * @author zhangh
 * @date 2018年8月27日下午5:43:57
 */
@Configuration
public class BaseConfig {

	@Bean
	public SpringUtil springUtil(){
		return new SpringUtil();
	}

	@Bean
	public ControllerMethodAdvice controllerMethodAdvice(){
		return new ControllerMethodAdvice();
	}

	@Bean
	public ServiceMethodAdvice serviceMethodAdvice(){
		return new ServiceMethodAdvice();
	}

	@Bean
	public DaoMethodHandle daoMethodHandle(){
		return new DaoMethodHandle();
	}
}