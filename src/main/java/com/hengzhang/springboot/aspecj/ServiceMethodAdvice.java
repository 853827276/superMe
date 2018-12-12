package com.hengzhang.springboot.aspecj;

import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.hengzhang.springboot.common.BaseDao;
import com.hengzhang.springboot.util.MapBeanExchangeUtil;
import com.hengzhang.springboot.util.SpringUtil;

/**
 * service 扫描机制
 * @author zhangh
 * @date 2018年8月27日下午6:06:18
 */
@Aspect
@Order(16)
public class ServiceMethodAdvice {

	private final String DAO = "Dao";
	private final String SERVICE = "ServiceImpl";

	@Pointcut("execution(* com.hengzhang.springboot.common.BaseService.*(..))")
	public void annoAspce() {
	}
	
	@Autowired
	private SpringUtil springUtil;
	
	@Autowired
	private DaoMethodHandle daoMethodHandle;

	/**
	 * 复杂业务逻辑处理
	 * 主要是根据主键 利用反射去间接调用DAO
	 * @author zhangh
	 * @date 2018年8月27日下午6:18:29
	 * @param joinpoint
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings({ "static-access" })
	@Around("annoAspce()")
    public Object doAround(ProceedingJoinPoint joinpoint) throws Throwable {
		Class<?> entityClass = JoinPointHelper.getClass(joinpoint).getClass();
		BaseDao baseDao = (BaseDao)springUtil.getBean(StringUtils.uncapitalize(StringUtils.remove(entityClass.getSimpleName(), SERVICE) + DAO));
		String methodName = JoinPointHelper.getMethodName(joinpoint);
		Annotation[] annos = JoinPointHelper.getMethod(joinpoint).getAnnotations();
		MethodHandle[] handles = daoMethodHandle.chooseMethodHandle(methodName, annos);
		Class<?> returnClass = JoinPointHelper.getMethodReturnType(joinpoint);
		return executeMethod(getAttrNames(methodName, annos), handles, getMethodArgValuesAndObjValue(baseDao, JoinPointHelper.getMethodArgValues(joinpoint)), returnClass);
	}

	/**
	 * 给属性赋值
	 * @author zhangh
	 * @date 2018年8月27日下午6:19:53
	 * @param methodName
	 * @param annos
	 * @return
	 */
	private String[] getAttrNames(String methodName, Annotation[] annos){
		if(annos != null && annos.length > 0){
			Optional<Annotation> op = Arrays.stream(annos).filter(anno -> anno instanceof ServiceMethod).findFirst();
			if(op.isPresent()){
				return ((ServiceMethod)op.get()).attrNames();
			}
		}
		return new String[]{};
	}

	private Object executeMethod(String[] attrNames, MethodHandle[] handles, List<Object> arguments, Class<?> returnClass) throws Throwable {
		if(handles == null || handles.length == 0){
			return null;
		}
		if(handles.length == 1){
			return handles[0].invokeWithArguments(arguments);
		}
		return MapBeanExchangeUtil.convertToBean2(returnClass, invokeWithArguments(attrNames, handles, arguments));
	}

	private Map<String, Object> invokeWithArguments(String[] attrNames, MethodHandle[] handles, List<Object> arguments){
		Map<String, Object> map = new HashMap<>();
		IntStream.range(0, attrNames.length).boxed().forEach(index -> {
			try {
				if(handles[index] != null)
					map.put(attrNames[index], handles[index].invokeWithArguments(arguments));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		});
		return map;
	}

	private List<Object> getMethodArgValuesAndObjValue(Object obj, Object[] args){
		List<Object> list = Arrays.asList(args).stream().collect(toList());
		list.add(0, obj);
		return list;
	}
}