package com.hengzhang.springboot.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验参数开关 只需要在方法前加上该注解即可
 * @author zhangh
 * @date 2018年7月18日上午10:01:32
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckParam {}
