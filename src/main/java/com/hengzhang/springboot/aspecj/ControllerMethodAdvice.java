package com.hengzhang.springboot.aspecj;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.hengzhang.springboot.common.BaseService;
import com.hengzhang.springboot.util.SpringUtil;


/**
 * controller 扫描处理
 * @author zhangh
 * @date 2018年8月27日下午5:57:23
 */
@Aspect
@Order(15)
public class ControllerMethodAdvice {

	private final String SERVICE = "ServiceImpl";
	private final String CONTROLLER = "Controller";

	@Pointcut("execution(* com.hengzhang.springboot.common.BaseController.*(..))")
	public void annoAspce() {
	}
	@Autowired
	private SpringUtil springUtil;

	@SuppressWarnings({ "static-access" })
	@Around("annoAspce()")
    public Object doAround(ProceedingJoinPoint joinpoint) throws Throwable {
		Class<?> entityClass = JoinPointHelper.getClass(joinpoint).getClass();
		Object[] methodArgs = JoinPointHelper.getMethodArgValues(joinpoint);
		methodArgs[methodArgs.length - 1] =  (BaseService)springUtil.getBean(StringUtils.uncapitalize(StringUtils.remove(entityClass.getSimpleName(), CONTROLLER) + SERVICE));
		return joinpoint.proceed(methodArgs);
	}

}