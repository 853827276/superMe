package com.hengzhang.springboot.redis;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置
 * @author zhangh
 * @date 2018年8月10日上午11:42:26
 */
@Configuration
@SuppressWarnings("rawtypes")
public class RedisConfig {
	
	/**
	 * 自定义序列化规则
	 * @author zhangh
	 * @date 2018年8月10日上午11:42:38
	 * @return
	 */
	@Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    /**
     * RedisTemplate
     * @author zhangh
     * @date 2018年8月10日上午11:42:54
     * @param redisConnectionFactory
     * @param fastJson2JsonRedisSerializer
     * @return
     * @throws Exception
     */
	@Bean
    public RedisTemplate<String,Object> initRedisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer fastJson2JsonRedisSerializer) throws Exception {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }
}
