package com.hengzhang.springboot.util;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * ArrayUtil 工具类
 * @author zhangh
 * @date 2018年8月27日下午6:08:08
 */
public class ArrayUtil {

	/**
	 * Long[]转换long[]
	 * @author zhangh
	 * @date 2018年8月27日下午6:08:48
	 * @param arr
	 * @return
	 */
	public static long[] longArrayToLongArray(Long[] arr){
		if(arr != null && arr.length > 0){
			return Arrays.stream(arr).mapToLong(id -> id).toArray();
		}
		return null;
	}

	/**
	 * int类型的数组转换为对象
	 * @author zhangh
	 * @date 2018年8月27日下午6:09:04
	 * @param arr
	 * @param mapper
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> R[] intArrayToRArray(int[] arr, IntFunction<? extends R> mapper, Class<?> clazz){
		List<R> list = Arrays.stream(arr).mapToObj(mapper).collect(toList());
		R[] t = (R[]) Array.newInstance(clazz, list.size());
		return list.toArray(t);
	}

	/**
	 * arr转换long[]
	 * @author zhangh
	 * @date 2018年8月27日下午6:09:15
	 * @param arr
	 * @param mapper
	 * @return
	 */
	public static <T> long[] arrToLArr(T[] arr, ToLongFunction<? super T> mapper){
		return ListUtil.streamToLArr(Arrays.stream(arr), mapper);
	}

	/**
	 * arr转换long[]
	 * @author zhangh
	 * @date 2018年8月27日下午6:09:25
	 * @param arr
	 * @param predicate
	 * @param mapper
	 * @return
	 */
	public static <T> long[] arrToLArr(T[] arr, Predicate<? super T> predicate, ToLongFunction<? super T> mapper){
		return ListUtil.streamToLArr(Arrays.stream(arr), predicate, mapper);
	}

	/**
	 * int[]过滤
	 * @author zhangh
	 * @date 2018年8月27日下午6:09:45
	 * @param arr
	 * @param predicate
	 * @return
	 */
	public static <T> int[] intArrToIArr(int[] arr, Predicate<? super Integer> predicate){
		return Arrays.stream(arr).boxed().filter(predicate).mapToInt(id -> id).toArray();
	}

	/**
	 * long[]过滤
	 * @author zhangh
	 * @date 2018年8月27日下午6:10:18
	 * @param arr
	 * @param predicate
	 * @return
	 */
	public static <T> long[] longArrToLArr(long[] arr, Predicate<? super Long> predicate){
		return Arrays.stream(arr).boxed().filter(predicate).mapToLong(id -> id).toArray();
	}

	/**
	 * arr转换long[]
	 * @author zhangh
	 * @date 2018年8月27日下午6:10:28
	 * @param stream
	 * @param mapper
	 * @return
	 */
	public static <T> long[] arrToLArr(Stream<T> stream, ToLongFunction<? super T> mapper){
		return ListUtil.streamToLArr(stream, mapper);
	}

	/**
	 * long[]转换Long[]
	 * @author zhangh
	 * @date 2018年8月27日下午6:10:39
	 * @param arr
	 * @return
	 */
	public static Long[] LongArrayTolongArray(long[] arr){
		if(arr != null && arr.length > 0){
			return Arrays.stream(arr).boxed().map(id -> id).toArray(Long[]::new);
		}
		return null;
	}

	/**
	 * String转换为Byte数组
	 * @author zhangh
	 * @date 2018年8月27日下午6:10:49
	 * @param str
	 * @return
	 */
	public static Byte[] strToByteArray(String str){
		if(str.endsWith(",")){
			str = str.substring(0, str.length() - 1);
		}
		return Arrays.stream(str.split(",")).map(s -> Byte.parseByte(s)).toArray(Byte[]::new);
	}

	/**
	 *  两个数组合并
	 * @author zhangh
	 * @date 2018年8月27日下午6:11:16
	 * @param first
	 * @param second
	 * @return
	 */
	public static long [] concat(long[] first, long[] second) {
		long[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * 求两个数组的差集
	 * @author zhangh
	 * @date 2018年8月27日下午6:11:30
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static long[] minus(long[] arr1, long[] arr2) {
		if(arr1 == null || arr1.length == 0){
			return arr1;
		}
		if(arr2 == null || arr2.length == 0){
			return arr1;
		}
	    List<Long> lst1 = Arrays.stream(arr1).boxed().collect(toList());
	    List<Long> lst2 = Arrays.stream(arr2).boxed().collect(toList());
	    lst1.removeAll(lst2);
	    return ListUtil.longListToLongArray(lst1);
	}

	/**
	 * 判断identityId是否在ids中存在
	 * @author zhangh
	 * @date 2018年8月27日下午6:11:39
	 * @param ids
	 * @param identityId
	 * @return
	 */
	public static boolean contains(long[] ids, long identityId){
		if(ids == null || ids.length == 0){
			return false;
		}
		return Arrays.stream(ids).filter(id -> id == identityId).findFirst().isPresent();
	}

	/**
	 * 去重
	 * @author zhangh
	 * @date 2018年8月27日下午6:11:51
	 * @param arr
	 * @return
	 */
	public static long[] distinct(long[] arr) {
	    return Arrays.stream(arr).distinct().toArray();
	}

	/**
	 * 判断数组里的所有元素是否都相等
	 * @author zhangh
	 * @date 2018年8月27日下午6:12:05
	 * @param arr
	 * @return 数组为空或大小为0，或不是全相等，返回false
	 */
	public static <T> boolean equalArr(T[] arr){
		if(arr == null || arr.length == 0){
			return false;
		}
		if(arr.length == 2){
			return arr[0] == arr[1];
		}
		for(int i=0; i<arr.length-2; i++){
		    if(arr[i] != arr[i+1]){
		        return false;
		    }
		}
		return true;
	}

}
