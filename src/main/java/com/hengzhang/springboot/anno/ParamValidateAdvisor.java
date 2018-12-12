package com.hengzhang.springboot.anno;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hengzhang.springboot.exception.ArgumentsException;
import com.hengzhang.springboot.util.ClassUtil;
import com.hengzhang.springboot.util.ListUtil;
import com.hengzhang.springboot.util.RegExpValidatorUtils;
import com.hengzhang.springboot.util.StringUtil;



/**
 * 参数校验的切面通知类
 * @author zhangh
 * @date 2018年7月26日下午3:44:41
 */
@Aspect
@Order(2)
@Component
public class ParamValidateAdvisor {
	private static Log logger = LogFactory.getLog(ParamValidateAdvisor.class);

	/**
	 * 定义一个校验参数的开关
	 * @author zhangh
	 * @date 2018年7月26日下午3:45:08
	 */
	@Pointcut("@annotation(com.hengzhang.springboot.anno.CheckParam)")
	public void pointCut() {

	}

	/**
	 * 切面回环处理 
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 * @author zhangh
	 */
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		logger.info("AOP开始校验参数");

		Object[] objs = pjp.getArgs();
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();

		Annotation[][] annos = method.getParameterAnnotations();
		boolean flag = validateParameterAnnotation(annos);
		if(!flag){
			return pjp.proceed(objs);
		}

		//判断注解是否存在 若存在则封装一下
		List<Param> params = AnnotationHelper.getParms(method,objs);
		if(ListUtil.isNotBlank(params)){
			for(Param param : params){
				String validRes = validateDetail(param);
				if(!StringUtils.isEmpty(validRes)){
					logger.info("客户端上报参数错误详细信息:{}"+validRes);
					throw new ArgumentsException(validRes);
				}
			}
		}
		return pjp.proceed(objs);

	}

	/**
	 * validate NotEmpty
	 * @author zhangh
	 * @date 2018年8月2日上午10:11:53
	 * @param param_Val
	 * @param annotation
	 * @param notEmpty
	 * @return
	 */
	private String validateNotEmpty(Object param_Val,Annotation annotation){
		if(StringUtil.isEmpty(param_Val)){
			NotEmpty notEmpty =(NotEmpty)annotation;
			return notEmpty.value();
		}
		return null;
	}

	private String validateRegular(Object param_Val,Annotation annotation){
		if(null == param_Val){
			Regular regular =(Regular)annotation;
			return regular.value();
		}
		return null;
	}
	
	private String validateNotNull(Object param_Val,Annotation annotation){
		if(null == param_Val){
			NotNull NotNull =(NotNull)annotation;
			return NotNull.value();
		}
		return null;
	}

	private String validateListNotNull(Object param_Val,Annotation annotation){
		ListNotNull ListNotNull =(ListNotNull)annotation;
		if(null == param_Val){
			return ListNotNull.value();
		}else{
			List<?> list = (List<?>) param_Val;
			if(ListUtil.isBlank(list)){
				return ListNotNull.value();
			}
		}
		return null;
	}
	
	private String validateSize(Object param_Val,Annotation annotation){
		Size size =(Size)annotation;
		if(param_Val ==null){
			return size.value();
		}else{
			String value = (String) param_Val;
			if(value.length() > size.max() || value.length() < size.min()){
				return size.value();
			}
		}
		return null;
	}
	
	private String validateValueMustInList(Object param_Val,Annotation annotation){
		ValueMustInList valueMustInList =(ValueMustInList)annotation;
		if (param_Val == null) {
			return valueMustInList.value();
		} else {
			String value = String.valueOf(param_Val);
			String[] mustList = valueMustInList.list();
			if (!Arrays.asList(mustList).contains(value)) {
				return valueMustInList.value();
			}
		}
		return null;
	}
	
	private String validateValidate(Param param,Annotation annotation) throws IllegalArgumentException, IllegalAccessException{
		Validate validate =(Validate)annotation;
		boolean isVali = validate.isValidate();
		if(isVali){
			if(validate.required() == true){
				return validateForm(param,validate);
			}
		}
		return null;
	}

	/**
	 * 具体的校验逻辑,返回对应的错误信息
	 * @author zhangh
	 * @date 2018年7月26日上午10:47:53
	 * @param param
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String validateDetail(Param param) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sb = new StringBuilder();
		Object param_Val = param.getValue();//参数值
		if(param.getAnno() instanceof Validate){			
			append(sb,validateValidate(param, param.getAnno()));
		}else if(param.getAnno() instanceof NotEmpty){
			append(sb,validateNotEmpty(param_Val, param.getAnno()));
		}else if(param.getAnno() instanceof NotNull){
			append(sb,validateNotNull(param_Val, param.getAnno()));
		}else if(param.getAnno() instanceof ListNotNull){
			append(sb,validateListNotNull(param_Val, param.getAnno()));
		}else if(param.getAnno() instanceof Size){
			append(sb,validateSize(param_Val, param.getAnno()));
		}else if (param.getAnno() instanceof ValueMustInList) {
			append(sb, validateValueMustInList(param_Val, param.getAnno()));
		}else if (param.getAnno() instanceof Regular) {
			append(sb, validateRegular(param_Val, param.getAnno()));
		}
		return sb.toString();
	}

	private void append(StringBuilder sb,String res){
		if(!StringUtils.isEmpty(res)){
			sb.append(res+" ");
		}
	}
	/**
	 * 验证是否有某个注解
	 * @param annos
	 * @param validate
	 * @return
	 */
	private boolean validateParameterAnnotation(Annotation[][] annos){
		boolean flag = false;
		for(Annotation[] at : annos){
			for(Annotation a : at){
				if(ClassUtil.existAnnotation(a)){
					flag = true;
				}
			}
		}
		return flag;
	}

	private static Field[] getAllFields(Object object){
		  Class<?> clazz = object.getClass();
		  List<Field> fieldList = new ArrayList<>();
		  while (clazz != null){
		    fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
		    clazz = clazz.getSuperclass();
		  }
		  Field[] fields = new Field[fieldList.size()];
		  fieldList.toArray(fields);
		  return fields;
		}

	/**
	 * 校验对象里的属性
	 * @author zhangh
	 * @date 2018年7月26日下午3:46:22
	 * @param param
	 * @param val
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String validateForm(Param param, Validate val) throws IllegalArgumentException, IllegalAccessException{
//		Class<?> clazz = param.getValue().getClass();
//		Field[] fields = clazz.getDeclaredFields();
		Field[] fields = getAllFields(param.getValue());
		StringBuilder sb = new StringBuilder();
		for (Field f : fields) {
			f.setAccessible(true);
			Object obj = f.get(param.getValue());
			Annotation[] annos = f.getAnnotations();
			if (ArrayUtils.isNotEmpty(annos)) {
				for (Annotation anno: annos) {
					if (anno instanceof NotEmpty) {
						append(sb, formValidateNotEmpty(f, obj, anno, val));
					} else if (anno instanceof NotNull) {
						append(sb, formValidateNotNull(obj, anno, val));
					} else if (anno instanceof ListNotNull) {
						append(sb, formValidateListNotNull(obj, anno, val));
					} else if (anno instanceof Size) {
						append(sb, formValidateSize(obj, anno, val));
					} else if (anno instanceof ValueMustInList) {
						append(sb, formValidateValueMustInList(obj, anno, val));
					} else if (anno instanceof Regular) {
						append(sb, formValidateRegular(obj, anno, val));
					}
				}
			}
		}
		return sb.toString();
	}
	
	private String formValidateNotEmpty(Field f,Object obj,Annotation anno , Validate val){
		NotEmpty notEmpty = (NotEmpty) anno;
		if (ClassUtil.checkGroups(val.value(), notEmpty.groups())) {
			Class<?> type = f.getType();
			if (type.isArray()) {
				Object[] arr = (Object[]) obj;
				if (ArrayUtils.isEmpty(arr)) {
					return notEmpty.value();
				}
			} else if (type.isPrimitive()) {
				if (type == int.class) {
					int intObj = (int) obj;
					if (intObj <= 0) {
						return notEmpty.value();
					}
				} else if (type == long.class) {
					long longObj = (long) obj;
					if (longObj <= 0) {
						return notEmpty.value();
					}
				}
			} else if (type == String.class) {
				if (StringUtils.isEmpty((String) obj)) {
					return notEmpty.value();
				}
			}
		}
		return null;
	}
	
	private String formValidateNotNull(Object obj,Annotation anno , Validate val){
		NotNull notNull = (NotNull) anno;
		if (ClassUtil.checkGroups(val.value(), notNull.groups())) {
			if (obj == null) {
				return notNull.value();
			}
		}
		return null;
	}
	
	private String formValidateRegular(Object obj,Annotation anno , Validate val){
		Regular regular = (Regular) anno;
		if (ClassUtil.checkGroups(val.value(), regular.groups())) {
			if (!RegExpValidatorUtils.match(regular.regx(), obj.toString())) {
				return regular.value();
			}
		}
		return null;
	}
	
	private String formValidateSize(Object obj,Annotation anno , Validate val){
		Size size = (Size) anno ;
		if (ClassUtil.checkGroups(val.value(), size.groups())) {
			if (obj == null) {
				return size.value();
			} else {
				String value = (String) obj;
				int maxLength = size.max();
				int minLength = size.min();
				if (value.length() > maxLength || value.length() < minLength) {
					return size.value();
				}
			}
		}
		return null;
	}
	
	private String formValidateListNotNull(Object obj,Annotation anno , Validate val){
		ListNotNull listNotNull = (ListNotNull) anno ;
		if (ClassUtil.checkGroups(val.value(), listNotNull.groups())) {
			
			if (obj == null) {
				return listNotNull.value();
			} else {
				List<?> list = (List<?>) obj;
				if (ListUtil.isBlank(list)) {
					return listNotNull.value();
				}
			}
		}
		return null;
	}
	
	private String formValidateValueMustInList(Object obj,Annotation anno , Validate val){
		ValueMustInList valueMustInList = (ValueMustInList) anno ;
		if (ClassUtil.checkGroups(val.value(), valueMustInList.groups())) {
			if (obj == null) {
				return valueMustInList.value();
			} else {
				String value = String.valueOf(obj);
				String[] mustList = valueMustInList.list();
				if (!Arrays.asList(mustList).contains(value)) {
					return valueMustInList.value();
				}
			}
		}
		return null;
	}
}
