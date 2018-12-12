package com.hengzhang.springboot.util;

import java.util.UUID;

/**
 * 随机生成字符串工具类
 * @author zhangh
 * @date 2018年8月28日下午4:23:29
 */
public class UUIDUtils {
	/**
	 * 随机生成id
	 * 
	 * @return
	 */
	public static String getId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
