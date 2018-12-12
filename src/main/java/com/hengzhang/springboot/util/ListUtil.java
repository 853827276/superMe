package com.hengzhang.springboot.util;
import static java.util.stream.Collectors.toList;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtil {

	/**
	 * 判断list是否为空 包含 null 和空对象
	 * @author zhangh
	 * @date 2018年7月19日上午11:22:40
	 * @param list
	 * @return
	 */
	public static boolean isBlank(List<?> list){
		return list==null || list.size()==0?true:false;
	}
	
	/**
	 * 判断list非空包含空对象
	 * @author zhangh
	 * @date 2018年7月19日上午11:24:41
	 * @param list
	 * @return
	 */
	public static boolean isNotBlank(List<?> list){
		return !isBlank(list);
	}

	/**合并list，返回去除重复元素后的新list
	 * @param list1
	 * @param list2
	 * @return list1和list2全为空，则返回null
	 */
	public static <T> List<T> removeListDupli(List<T> list1, List<T> list2){
		if(list1 == null && list2 == null){
			return null;
		}
		if(list1 == null){
			return list2;
		}
		if(list2 == null){
			return list1;
		}
		List<T> result = new ArrayList<>(list1);
		result.addAll(list2);
		return result.stream().distinct().collect(Collectors.toList());
	}

	/**Collection<Long>转换为long[]
	 * @param list
	 * @return
	 */
	public static long[] longListToLongArray(Collection<Long> list){
		if(list == null){
			return null;
		}
		return list.stream().mapToLong(id -> id).toArray();
	}

	/**Collection<Long>转换为Long[]
	 * @param list
	 * @return
	 */
	public static Long[] longListToLongArray2(Collection<Long> list){
		if(list == null){
			return null;
		}
		return list.stream().map(id -> id).toArray(Long[]::new);
	}

	/**Collection<Long>转换为long[]
	 * @param list
	 * @return
	 */
	public static int[] intListToIntArray(Collection<Integer> list){
		if(list == null){
			return null;
		}
		return list.stream().mapToInt(id -> id).toArray();
	}

	/**Collection<Long>转换为Long[]
	 * @param list
	 * @return
	 */
	public static Integer[] intListToIntArray2(Collection<Integer> list){
		if(list == null){
			return null;
		}
		return list.stream().map(id -> id).toArray(Integer[]::new);
	}

	/**stream转换long[]
	 * @param stream
	 * @param mapper
	 * @return
	 */
	public static <T> long[] streamToLArr(Stream<T> stream, ToLongFunction<? super T> mapper){
		return stream.mapToLong(mapper).toArray();
	}

	/**stream转换int[]
	 * @param stream
	 * @param mapper
	 * @return
	 */
	public static <T> int[] streamToIArr(Stream<T> stream, ToIntFunction<? super T> mapper){
		return stream.mapToInt(mapper).toArray();
	}

	/**stream转换long[]
	 * @param stream
	 * @param mapper
	 * @return
	 */
	public static <T> long[] streamToLArr(Stream<T> stream, Predicate<? super T> predicate, ToLongFunction<? super T> mapper){
		return stream.filter(predicate).mapToLong(mapper).toArray();
	}

	/**stream转换Long[]
	 * @param stream
	 * @param mapper
	 * @return
	 */
	public static <T, R> Long[] streamToLArr2(Stream<T> stream, Function<? super T, ? extends R> mapper){
		return stream.map(mapper).toArray(Long[]::new);
	}

	/**stream转换Long[]
	 * @param stream
	 * @param mapper
	 * @return
	 */
	public static <T, R> Integer[] streamToIArr2(Stream<T> stream, Function<? super T, ? extends R> mapper){
		return stream.map(mapper).toArray(Integer[]::new);
	}

	/**list转换long[]
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> long[] listToLArr(Collection<T> list, ToLongFunction<? super T> mapper){
		if(list == null || list.size() == 0){
			return null;
		}
		return streamToLArr(list.stream(), mapper);
	}

	/**list转换int[]
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> int[] listToIArr(Collection<T> list, ToIntFunction<? super T> mapper){
		if(list == null || list.size() == 0){
			return null;
		}
		return streamToIArr(list.stream(), mapper);
	}

	/**list转换Long[]
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T, R> Long[] listToLArr2(Collection<T> list, Function<? super T, ? extends R> mapper){
		if(list == null || list.size() == 0){
			return null;
		}
		return streamToLArr2(list.stream(), mapper);
	}

	/**list转换Long[]
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T, R> Integer[] listToIArr2(Collection<T> list, Function<? super T, ? extends R> mapper){
		if(list == null || list.size() == 0){
			return null;
		}
		return streamToIArr2(list.stream(), mapper);
	}

	/**list转换List<Integer>
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> List<Integer> listToIList(Collection<T> list, Function<? super T, Integer> mapper){
		if(list == null || list.size() == 0){
			return null;
		}
		return list.stream().map(mapper).collect(toList());
	}

	/**stream转换List<Integer>
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> List<Integer> streamToIList(Stream<T> stream, Function<? super T, Integer> mapper){
		if(stream == null){
			return null;
		}
		return stream.map(mapper).collect(toList());
	}

	/**list转换List<Long>
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> List<Long> listToLList(Collection<T> list, Function<? super T, Long> mapper){
		if(list == null || list.size() == 0){
			return null;
		}
		return list.stream().map(mapper).collect(toList());
	}

	/**stream转换List<Long>
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> List<Long> streamToLList(Stream<T> stream, Function<? super T, Long> mapper){
		if(stream == null){
			return null;
		}
		return stream.map(mapper).collect(toList());
	}

	/**list<T>类型强转换
	 * @param list
	 * @param mapper
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> List<R> listChange(Collection<T> list, Class<R> r){
		if(list == null || list.size() == 0){
			return null;
		}
		return list.stream().map(t -> (R)t).collect(toList());
	}

	/**
	 * 去重
	 */
	public static <T> int[] distinct(List<T> list, ToIntFunction<? super T> mapper) {
		if(list == null || list.size() == 0){
			return null;
		}
	    return list.stream().mapToInt(mapper).distinct().toArray();
	}

	/**
	 * 去重
	 */
	public static <T> List<Integer> distinctReturnList(List<T> list, ToIntFunction<? super T> mapper) {
		if(list == null || list.size() == 0){
			return null;
		}
		return list.stream().mapToInt(mapper).distinct().boxed().collect(toList());
	}

	/**
	 * 条件去重
	 * @param keyExtractor
	 * @return
	 */
	public static <T> List<T> distinct2(List<T> list, Function<? super T, ?> keyExtractor) {
		if(list == null || list.size() == 0){
			return null;
		}
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		Predicate<T> p = t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		return list.stream().filter(p).collect(toList());
	}

	/**
	 * 条件去重(stream)
	 * @param keyExtractor, stream<T>
	 * @return
	 */
	public static <T> Stream<T> distinct2ReStream(Stream<T> stream, Function<? super T, ?> keyExtractor) {
		if(stream == null){
			return null;
		}
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		Predicate<T> p = t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		return stream.filter(p);
	}

	/**
	 * 获取list对象的泛型类型
	 * @param type
	 * @return
	 */
	public static Class<?> getListEleType(ParameterizedType type){
		return (Class<?>)type.getActualTypeArguments()[0];
	}

}
