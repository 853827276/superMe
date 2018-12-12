package com.hengzhang.springboot.redis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * redis 工具类
 * @author zhangh
 * @date 2018年8月10日下午12:27:35
 */
@Component
public class RedisUtil {
	
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		Object result = null;
		ValueOperations<String, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		if (result == null) {
			return null;
		}
		return result;
	}
	
	public int getInt(final String key) {
		Object result = null;
		ValueOperations<String, Object> operations = redisTemplate
				.opsForValue();
		result = operations.get(key);
		return result==null?0:Integer.parseInt(result.toString());
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean hmset(String key, Map<String, String> value) {
		boolean result = false;
		try {
			redisTemplate.opsForHash().putAll(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Map<Object, Object> hmget(String key) {
		Map<Object, Object> result = null;
		try {
			result = redisTemplate.opsForHash().entries(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
