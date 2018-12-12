package com.hengzhang.springboot.common;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 公共dao层
 * @author zhangh
 * @date 2018年8月27日下午1:46:25
 * @param <T>
 */
public interface BaseDao {


	public void add(@Param("domain") BaseEntity baseEntity);
	
	public void adds(@Param("domains") List<? extends BaseEntity> baseEntitys);

	public void update(@Param("domain") BaseEntity baseEntity);
	
	public void updateStatus(@Param("id") String id , @Param("status") String status);
	
	public Map<String, Object> findById(@Param("id") String id);
	
	public List<Map<String, Object>> findByIds(@Param("ids") List<String> ids);
	
	public void deleteById(@Param("id") String id);
	
	public void deleteByIds(@Param("ids") List<String> id);
	
	public List<Map<String, Object>> queryForList(@Param("domain") BaseEntity baseEntity);

	public <M> List<M> queryForPage(@Param("domain") BaseEntity baseEntity);
	
	public Integer queryForPageCount(@Param("domain") BaseEntity baseEntity);
	
	default public Integer queryForPageSize(@Param("domain") BaseEntity baseEntity){return baseEntity.getPageSize();};
	
	default public Integer queryForPageNum(@Param("domain") BaseEntity baseEntity) {return baseEntity.getPage();};
}