package com.hengzhang.springboot.aspecj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 依次调用服务层的其它方法（方法名methodNames），把结果保存到返回对象的属性里（属性名attrNames）。
 * methodNames数组比attrNames数组少一个元素，因为最后一个要执行的方法就是被注解的方法本身。
 * @author zhangh
 * @date 2018年8月27日下午3:32:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceMethod {

	public abstract String[] methodNames();

	public abstract String[] attrNames();

}
