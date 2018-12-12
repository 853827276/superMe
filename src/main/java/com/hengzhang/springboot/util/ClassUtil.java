package com.hengzhang.springboot.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.hengzhang.springboot.anno.ListNotNull;
import com.hengzhang.springboot.anno.NotEmpty;
import com.hengzhang.springboot.anno.NotNull;
import com.hengzhang.springboot.anno.Regular;
import com.hengzhang.springboot.anno.Size;
import com.hengzhang.springboot.anno.Validate;
import com.hengzhang.springboot.anno.ValueMustInList;


/**
 * class 工具类
 * @author zhangh
 * @date 2018年7月26日下午3:38:55
 */
public class ClassUtil {

	private static List<Class<?>> classList = new ArrayList<>();
	static{
		classList.add(NotEmpty.class);
		classList.add(NotNull.class);
		classList.add(Validate.class);
		classList.add(ListNotNull.class);
		classList.add(Size.class);
		classList.add(ValueMustInList.class);
		classList.add(Regular.class);
	}
	public static List<Class<?>> getAnnotationList(){
		return classList;
	}
	
	/**
	 * 验证是否存在自己定义的注解
	 * @author zhangh
	 * @date 2018年7月27日下午5:23:49
	 * @param annotation
	 * @return
	 */
	public static boolean existAnnotation(Annotation annotation){
		for (Class<?> c : getAnnotationList()) {
			if(annotation.annotationType()==c){
				return true;
			}
		}
		return false;
	}
	/**
	 * 校验AOP 参数的group
	 * @author zhangh
	 * @date 2018年7月26日下午3:39:14
	 * @param groups1
	 * @param groups2
	 * @return
	 */
	public static boolean checkGroups(Class<?>[] groups1,Class<?>[] groups2){
		boolean b=false;
		if(groups1==null||groups2==null){
			b=true;
		}		
		for (Class<?> class2 : groups2) {
			if(groups1[0].getName().equals(class2.getName())){
				b=true;
				break;
			}
		}		
		return b;
	}
}
