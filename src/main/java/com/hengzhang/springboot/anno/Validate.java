package com.hengzhang.springboot.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义AOP 校验参数
 * @author zhangh
 * @date 2018年7月26日上午10:46:53
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {
	public boolean isValidate() default true;
	public boolean required() default false;
	public String msg() default "";
	public Class<?>[] value() default {};
}
