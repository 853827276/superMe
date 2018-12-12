package com.hengzhang.springboot.anno;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER })
public @interface ValueMustInList {

	/**
	 * 验证失败时的提示语
	 * @return
	 */
	public String value();
	/**
	 * 合法的值数组
	 * @return
	 */
	public String[] list();
	/**
	 * 验证分组
	 * @return
	 */
	public Class<?>[] groups() default{};
}
