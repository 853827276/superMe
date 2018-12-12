package com.hengzhang.springboot.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 长度注解
 * @author zhangh
 * @date 2018年8月1日下午2:53:54
 */
@Target({ ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Size {
	int min() default 0;
	int max() default Integer.MAX_VALUE;
	Class<?>[] groups() default { };
	String value();
}
