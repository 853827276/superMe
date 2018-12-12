package com.hengzhang.springboot.common;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.hengzhang.springboot.anno.NotEmpty;
import com.hengzhang.springboot.anno.validate.Update;

/**
 * 实体类的顶层
 * 
 * @author zhangh
 * @date 2018年8月26日上午9:14:06
 */
public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3844750156428273501L;
	//通用主键字段
	@NotEmpty(value = "id 不能为空",groups={Update.class})
	private String id;//主键
	private Integer page = 1;
	private Integer pageSize = 20;
	private String startCreateTime;
	private String endCreateTime;
	private String startUpdateTime;
	private String endUpdateTime;
	private String orderBy;
	private String orderByFlag;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}	
	
	public Integer getPageStart() {
		return pageSize * (page - 1);
	}

	
	public String getStartCreateTime() {
		return startCreateTime;
	}
	public void setStartCreateTime(String startCreateTime) {
		this.startCreateTime = startCreateTime;
	}
	public String getEndCreateTime() {
		return endCreateTime;
	}
	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}
	public String getStartUpdateTime() {
		return startUpdateTime;
	}
	public void setStartUpdateTime(String startUpdateTime) {
		this.startUpdateTime = startUpdateTime;
	}
	public String getEndUpdateTime() {
		return endUpdateTime;
	}
	public void setEndUpdateTime(String endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}
	public BaseEntity() {
		super();
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOrderByFlag() {
		return orderByFlag;
	}
	public void setOrderByFlag(String orderByFlag) {
		this.orderByFlag = orderByFlag;
	}
	public BaseEntity(String id) {
		super();
		this.id = id;
	}
	/**
	 * 便于日志查看
	 * @author zhangh
	 * @date 2018年8月26日上午9:14:08
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
