package com.hengzhang.springboot.util;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * request 工具类
 * @author zhangh
 * @date 2018年7月31日上午11:35:09
 */
public class RequestContextHolderUtil {

	public static ServletRequestAttributes getRequestAttributes() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
	}

	public static ServletContext getServletContext() {
		return ContextLoader.getCurrentWebApplicationContext().getServletContext();
	}
	/**
	 * 获取session
	 * @author zhangh
	 * @date 2018年7月31日上午11:35:26
	 * @return
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}
	/**
	 * 获取request
	 * @author zhangh
	 * @date 2018年7月31日上午11:35:34
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return getRequestAttributes().getRequest();
	}

	/**
	 * 获取response
	 * @author zhangh
	 * @date 2018年7月31日上午11:35:48
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return getRequestAttributes().getResponse();
	}
	/**
	 * 从cookie中获取值
	 * @author zhangh
	 * @date 2018年7月31日上午11:36:06
	 * @param key
	 * @return
	 */
	public static String getCookieValue(String key) {
		Assert.notNull(key,"");
		Cookie[] cookies = getRequest().getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 往cookie 中添加值
	 * @author zhangh
	 * @date 2018年7月31日上午11:36:19
	 * @param key
	 * @param value
	 * @param maxage
	 */
	public static void setCookieValue(String key, String value, Integer maxage) {
		Assert.notNull(key,"");
		
		Cookie c = new Cookie(key, value);
		c.setPath("/");
		if(maxage != null) {
			c.setMaxAge(maxage);
		}
		getResponse().addCookie(c);
	}
	/**
	 * 清除cookie 中的值
	 * @author zhangh
	 * @date 2018年7月31日下午2:17:30
	 * @param key
	 */
	public static void clearValue(String key) {
		setCookieValue(key, null, 0);
	}
	
	/**
	 * 获取用户的有效IP
	 * @author zhangh
	 * @date 2018年7月31日下午3:31:17
	 * @return
	 */
	public static String getIpAddress() {
		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}  
}