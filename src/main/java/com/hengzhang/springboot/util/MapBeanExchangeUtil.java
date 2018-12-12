package com.hengzhang.springboot.util;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;


/**
 * javabean和map互换
 * @author zhangh
 * @date 2018年8月27日下午2:58:50
 */
public class MapBeanExchangeUtil {

	public static <T> T convertToBean2(Class<T> type, Map<String, Object> map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		T obj = type.newInstance();
		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
		Arrays.stream(propertyDescriptors).forEach(descriptor -> {
			String propertyName = descriptor.getName();
			if (map.containsKey(propertyName)) {
				try{
					Object value = map.get(propertyName);
					if(value == null)
						return;
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(obj, args);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		return obj;
	}
}