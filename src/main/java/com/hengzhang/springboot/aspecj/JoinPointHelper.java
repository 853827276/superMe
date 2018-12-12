package com.hengzhang.springboot.aspecj;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * spring切面帮助类
 * @author zhangh
 * @date 2018年8月27日下午6:05:41
 */
public class JoinPointHelper {

	/**
	 * 方法描述：获取拦截方法的签名
	 *
	 * @param joinpoint
	 * @return
	 */
	public static String getMethodName(ProceedingJoinPoint joinpoint){
		Method method = getMethod(joinpoint);
		return method.getName();
	}

	/**
	 * 方法描述：获取拦截方法的注解
	 *
	 * @param joinpoint
	 * @return
	 */
	public static Annotation[] getMethodAnnotations(ProceedingJoinPoint joinpoint){
		Method method = getMethod(joinpoint);
		return method.getAnnotations();
	}

	/**
	 * 方法描述：获取被拦截类自身定义的所有方法对象
	 *
	 * @param joinpoint
	 * @return
	 */
	public static Method[] getMethods(ProceedingJoinPoint joinpoint){
		Class<?> clazz = joinpoint.getTarget().getClass();
		return clazz.getDeclaredMethods();
	}

	/**
	 * 方法描述：获取被拦截类的类名
	 *
	 * @param joinpoint
	 * @return
	 */
	public static String getClassName(ProceedingJoinPoint joinpoint){
		Class<?> clazz = joinpoint.getTarget().getClass();
		return clazz.getName();
	}

	/**
	 * 方法描述：获取被拦截的类
	 *
	 * @param joinpoint
	 * @return
	 */
	public static Object getClass(ProceedingJoinPoint joinpoint){
		return joinpoint.getTarget();
	}

	/**获取被拦截的类的属性值
	 * @param joinpoint
	 * @param attributeName 属性名
	 * @return
	 */
	public static Object getAttribute(ProceedingJoinPoint joinpoint, String attributeName){
		try{
			Field f = joinpoint.getTarget().getClass().getDeclaredField(attributeName);//获取属性-->Field
		    f.setAccessible(true);//如果是私有的 先要设置可访问
		    return f.get(joinpoint.getTarget());//获取值,这个get()方法重点是参数,参数是你要获取的类
		}catch(Exception e){
			try{
				Field f = joinpoint.getTarget().getClass().getSuperclass().getDeclaredField(attributeName);//获取父类属性-->Field
			    f.setAccessible(true);
			    return f.get(joinpoint.getTarget());
			}catch(Exception e2){
		    	e2.printStackTrace();
		    	return null;
		    }
		}
	}

	/**获取被拦截的参数的属性值
	 * @param param 被拦截的参数
	 * @param attributeName 属性名
	 * @return
	 */
	public static Object getAttribute(Object param, String attributeName){
		try{
			Field f = param.getClass().getDeclaredField(attributeName);//获取属性-->Field
			f.setAccessible(true);//如果是私有的 先要设置可访问
			return f.get(param);//获取值,这个get()方法重点是参数,参数是你要获取的类
		}catch(Exception e){
			try{
				Field f = param.getClass().getSuperclass().getDeclaredField(attributeName);//获取父类属性-->Field
				f.setAccessible(true);
				return f.get(param);
			}catch(Exception e2){
				e2.printStackTrace();
				return null;
			}
		}
	}

	/**设置被拦截的参数的属性值
	 * @param param 被拦截的参数
	 * @param attributeName 属性名
	 * @return
	 */
	public static Object setAttribute(Object param, String attributeName, Object newValue){
		try{
			Field f = param.getClass().getDeclaredField(attributeName);
			f.setAccessible(true);
			f.set(param, newValue);
			return param;
		}catch(Exception e){
			try{
				Field f = param.getClass().getSuperclass().getDeclaredField(attributeName);//获取父类属性-->Field
				f.setAccessible(true);
				f.set(param, newValue);
				return param;
			}catch(Exception e2){
				e2.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 方法描述：获取被拦截类的被拦截方法对象
	 *
	 * @param joinpoint
	 * @return
	 */
	public static Method getMethod(ProceedingJoinPoint joinpoint){
		Signature signature = joinpoint.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		return methodSignature.getMethod();
	}

	/**
	 * 方法描述：获取被拦截类的被拦截方法的返回值类型
	 *
	 * @param joinpoint
	 * @return
	 */
	public static Class<?> getMethodReturnType(ProceedingJoinPoint joinpoint){
		Method method = getMethod(joinpoint);
		return (Class<?>) method.getReturnType();
	}

	/**
	 * 方法描述：获取被拦截类的被拦截方法的返回值类型，带泛型
	 *
	 * @param joinpoint
	 * @return
	 */
	public static ParameterizedType getMethodGenericReturnType(ProceedingJoinPoint joinpoint){
		Method method = getMethod(joinpoint);
		return (ParameterizedType) method.getGenericReturnType();
	}

	/**
	 * 方法描述：获取拦截方法的所有输入参数值
	 *
	 * @param joinpoint
	 * @return
	 */
	public static Object[] getMethodArgValues(ProceedingJoinPoint joinpoint){
		return joinpoint.getArgs();
	}

}
