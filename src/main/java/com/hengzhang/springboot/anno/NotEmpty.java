package com.hengzhang.springboot.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验null(包括字符串，对象，基本类型)
 * @author zhangh
 * @date 2018年7月18日上午10:01:32
 */
@Target({ ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotEmpty {
	String value();
	Class<?>[] groups() default {};
}
