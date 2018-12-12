package com.hengzhang.springboot.util;

import java.util.List;

public class PageInfo<T> {

	private int page = MyEnum.PAGE_INIT_VALUE.getCode();
	private int pageSize = MyEnum.PAGESIZE_INIT_VALUE.getCode();
	private int totalPage;
	private int totalCount;
	private int startPage;
	private List<T> dataList;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize =pageSize;
	}
	
	public int getStartPage() {
		return startPage;
	}

	public void setStartPage() {
		this.startPage = (page-1)*pageSize;
	}

	public int getTotalPage() {
		if(totalCount%pageSize==0){
			this.totalPage = totalCount/pageSize;
		}else{
			this.totalPage = totalCount/pageSize + 1;
		}
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public PageInfo() {
		super();
	}

	public PageInfo(int page, int pageSize, int totalCount, List<T> dataList) {
		super();
		this.page = page;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.totalPage = getTotalPage();
		this.dataList = dataList;
	}
	
}
