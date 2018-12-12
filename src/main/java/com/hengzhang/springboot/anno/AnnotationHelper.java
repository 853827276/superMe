package com.hengzhang.springboot.anno;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.hengzhang.springboot.util.ClassUtil;

/**
 * 注解帮助类
 * @author zhangh
 * @date 2018年7月26日下午3:42:25
 */
public class AnnotationHelper {
	
	/**
	 * 获取MethodSignature
	 * @author zhangh
	 * @date 2018年7月26日下午3:42:49
	 * @param point
	 * @return
	 */
	public static MethodSignature getMethodSignature(ProceedingJoinPoint point){
		MethodSignature sign = (MethodSignature) point.getSignature();
		return sign;
	}
	
	/**
	 * 获取controller 的名称
	 * @author zhangh
	 * @date 2018年8月3日下午3:19:24
	 * @param point
	 * @return
	 */
	public static String getControllerName(ProceedingJoinPoint point){
		return point.getTarget().getClass().getSimpleName();
	}
	
	/**
	 * 获取代理对象的方法对象
	 * @author zhangh
	 * @date 2018年7月31日上午10:13:27
	 * @param point
	 * @return
	 */
	public static Method getMethod(ProceedingJoinPoint point){
		return AnnotationHelper.getMethodSignature(point).getMethod();
	}
	
	/**
	 * 获取代理方法上的注解对象
	 * @author zhangh
	 * @date 2018年7月31日上午10:17:24
	 * @param point
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint point,Class<T> annotationClass){
		return getMethod(point).getAnnotation(annotationClass);
	}
	
	/**
	 * 获取参数列表
	 * @author zhangh
	 * @date 2018年7月26日下午3:42:59
	 * @param point
	 * @return
	 */
	public static Object[] getArgs(ProceedingJoinPoint point){
		return point.getArgs();
	}	

	public static Class<?>[] getParameterTypes(ProceedingJoinPoint point){
		return getMethod(point).getParameterTypes();
	}
	
	/**
	 * 封装参数类型
	 * @author zhangh
	 * @date 2018年8月3日下午4:21:32
	 * @param point
	 * @return
	 */
	public static Object[] getParameterTypeSimpleName(ProceedingJoinPoint point){
		Class<?>[] paramObj = getParameterTypes(point);
		if(paramObj !=null){
			Object[] arr = new Object[paramObj.length];
			for (int i=0;i<paramObj.length;i++) {
				arr[i]=paramObj[i].getSimpleName();
			}
			return arr;
		}
		return null;
	}
	
	public static Object[] getParameterTypeName(ProceedingJoinPoint point){
		Class<?>[] paramObj = getParameterTypes(point);
		if(paramObj !=null){
			Object[] arr = new Object[paramObj.length];
			for (int i=0;i<paramObj.length;i++) {
				arr[i]=paramObj[i].getName();
			}
			return arr;
		}
		return null;
	}
	
	/**
	 * 获取参数的描述
	 * @author zhangh
	 * @date 2018年7月26日下午3:43:12
	 * @param method
	 * @param objs
	 * @return
	 */
	public static List<Param> getParms(Method method,Object[] objs){
		Annotation[][] annos = method.getParameterAnnotations();
		Class<?>[] paramTypes = method.getParameterTypes();
		List<Param> params = new ArrayList<Param>();
		for(int i=0;i<annos.length;i++){
			for(int j=0;j<annos[i].length;j++){
				//如果出现指定的注解类型
				if(ClassUtil.existAnnotation(annos[i][j])){
 					Param param = new Param(paramTypes[i].getSimpleName(), paramTypes[i].getName(), paramTypes[i], objs[i], annos[i][j]);//名称//参数类型//参数值//筛选出的注解
					params.add(param);
				}
			}
		}
		return params;
	}
}