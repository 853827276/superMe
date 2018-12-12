package com.hengzhang.springboot.anno;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.hengzhang.springboot.exception.LoginException;
import com.hengzhang.springboot.util.MyEnum;
import com.hengzhang.springboot.util.RequestContextHolderUtil;
import com.hengzhang.springboot.util.StringUtil;

/**
 * 处理登陆逻辑
 * @author zhangh
 * @date 2018年9月6日上午11:02:10
 */
@Aspect
@Order(1)
@Component
public class loginValidateAdvisor {
	private static Log logger = LogFactory.getLog(loginValidateAdvisor.class);
	
	private static List<String> whiteList = new ArrayList<>();
	
	static{
		whiteList.add("/login");
		whiteList.add("/logout");
	}
	/**
	 * 定义一个校验参数的开关
	 * @author zhangh
	 * @date 2018年7月26日下午3:45:08
	 */
	@Pointcut("@annotation(com.hengzhang.springboot.anno.CheckLogin)")
	public void pointCut() {

	}

	/**
	 * 切面回环处理 
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 * @author zhangh
	 */
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		logger.info("AOP开始校验登陆");
		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		String url = request.getRequestURL().toString();
		String uid = RequestContextHolderUtil.getCookieValue(MyEnum.USER_COOK_KEY.getMsg());
		if(StringUtil.isNullOrEmpty(uid)){	
		    if(!whiteList.contains(url)){		    	
		    	throw new LoginException("很抱歉，请您先登陆");
		    }
		}
		return pjp.proceed(pjp.getArgs());
	}
}
