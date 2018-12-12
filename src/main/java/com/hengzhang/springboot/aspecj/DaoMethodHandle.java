package com.hengzhang.springboot.aspecj;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.hengzhang.springboot.common.BaseDao;
import com.hengzhang.springboot.common.BaseEntity;

/**
 * 生成baseDao内所有方法的句柄，会把句柄保存在map里，之后可以直接从map获取
 * @author zhangh
 * @date 2018年8月27日下午5:57:47
 */
public class DaoMethodHandle {

	Map<String, MethodHandle> map = new ConcurrentHashMap<>();//baseDao对象里的所有方法句柄

	/**
	 * 拦截方法以及对应的注解
	 * @author zhangh
	 * @param methodName 被拦截的方法名
	 * @param annos 被拦截的方法上的注解
	 * @return
	 */
	public MethodHandle[] chooseMethodHandle(String methodName, Annotation[] annos) {
		String[] methodNames  = getMethodNames(methodName, annos);
		return Arrays.stream(methodNames).map(mName -> {
			if(map.get(mName) == null){
				String className = this.getClass().getPackage().getName() + "." + StringUtils.capitalize(mName) + "MethodHandle";
				try{
					Class<?> pp = Class.forName(className);
					MethodHandleStrategy methodHandleStrategy = (MethodHandleStrategy) pp.newInstance();
					map.put(mName, methodHandleStrategy.chooseMethodHandle(MethodHandles.lookup(), mName));
				}catch(Exception e){
					return null;
				}
			}
			return map.get(mName);
		}).toArray(MethodHandle[]::new);
	}

	/**
	 * 获取ServiceMethod注解上的需要执行的方法名，和拦截的方法名一起组成字符串数组返回
	 * @author zhangh
	 * @date 2018年8月27日下午5:59:44
	 * @param methodName
	 * @param annos
	 * @return
	 */
	private String[] getMethodNames(String methodName, Annotation[] annos){
		if(annos != null && annos.length > 0){
			Optional<Annotation> op = Arrays.stream(annos).filter(anno -> anno instanceof ServiceMethod).findFirst();
			if(op.isPresent()){
				String[] methodNames = ((ServiceMethod)op.get()).methodNames();
				String[] result = Arrays.copyOf(methodNames, methodNames.length + 1);
				result[methodNames.length] = methodName;
				return result;
			}
		}
		return new String[]{methodName};
	}

}

interface MethodHandleStrategy {
	MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception;
}

class AddMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(void.class, BaseEntity.class));
	}
}

class UpdateMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(void.class, BaseEntity.class));
	}
}

class UpdateStatusMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(void.class, String.class, String.class));
	}
}

class FindByIdMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(Map.class, String.class));
	}
}

class FindByIdsMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(Map.class, List.class));
	}
}

class DeleteByIdMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(void.class, String.class));
	}
}

class DeleteByIdsMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(void.class, List.class));
	}
}

class QueryForListMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(List.class, BaseEntity.class));
	}
}

class QueryForPageMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(List.class, BaseEntity.class));
	}
}

class QueryForPageCountMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(Integer.class, BaseEntity.class));
	}
}

class QueryForPageSizeMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(Integer.class, BaseEntity.class));
	}
}
class QueryForPageNumMethodHandle implements MethodHandleStrategy {
	@Override
	public MethodHandle chooseMethodHandle(MethodHandles.Lookup lookup, String methodName) throws Exception {
		return lookup.findVirtual(BaseDao.class, methodName, MethodType.methodType(Integer.class, BaseEntity.class));
	}
}