package com.hengzhang.springboot.common;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hengzhang.springboot.aspecj.ServiceMethod;

/**
 * 公用service层
 * 
 * @author zhangh
 * @date 2018年8月26日上午9:14:36
 * @param <T>
 */
public interface BaseService {

	/**
	 * 添加
	 * @author zhangh
	 * @date 2018年8月28日上午8:37:41
	 * @param baseEntity
	 */
	default public void add(BaseEntity baseEntity){ }
	
	/**
	 * 修改
	 * @author zhangh
	 * @date 2018年8月28日上午8:37:48
	 * @param baseEntity
	 */
	default public void update(BaseEntity baseEntity){ }

	/**
	 * 修改状态
	 * @author zhangh
	 * @date 2018年8月28日上午8:37:54
	 * @param id
	 * @param status
	 */
	default public void updateStatus(String id, String status){ }
	
	/**
	 * 根据主键查找
	 * @author zhangh
	 * @date 2018年8月28日上午8:38:51
	 * @param id
	 * @return
	 */
	default public Object findById(String id){return null;}

	/**
	 * 根据主键删除
	 * @author zhangh
	 * @date 2018年8月28日上午8:39:10
	 * @param id
	 * @return
	 */
	default public void deleteById(String id){ }
	
	/**
	 * 根据主键批量删除
	 * @author zhangh
	 * @date 2018年8月28日下午6:41:00
	 * @param id
	 * @return
	 */
	default public void deleteByIds(List<String> id){ }

	/**
	 * 多条件非分页查询
	 * @author zhangh
	 * @date 2018年8月28日上午8:39:29
	 * @param queryDomain
	 * @return
	 */
	default public <T> List<T> queryForList(BaseEntity queryDomain){ return null; }

	/**
	 * 多条件分页查询
	 * @author zhangh
	 * @date 2018年8月28日上午8:39:52
	 * @param queryDomain
	 * @return
	 */
	@ServiceMethod(methodNames = {"queryForPageNum", "queryForPageSize","queryForPageCount"}, attrNames = {"page","pageSize", "totalCount", "dataList"})
	default public <T> PageInfo<T> queryForPage(BaseEntity queryDomain){
		return null;
	}
}