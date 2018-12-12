package com.hengzhang.springboot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

public class ResultWrapper {
	
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String RESULT_MESSAGE_SAVESUCCESS = "保存成功";
	public static final String RESULT_MESSAGE_DALETESUCCESS = "删除成功";
	/**
	 * 包装带分页列表的数据成为Map结构
	 * 
	 * @param list
	 * @param status
	 * @param message
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> wrapListPage(List<?> list, String status, String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> pageConfig = new HashMap<String, Object>();

		if (list == null) {
			pageConfig.put("page", 0);
			pageConfig.put("pageSize", 0);
			pageConfig.put("totalPage", 0);
			pageConfig.put("totalCount", 0);
			list = new ArrayList<Object>();
		} else {
			PageInfo<Object> pi = new PageInfo<Object>((List<Object>) list);

			pageConfig.put("page", pi.getPageNum());
			pageConfig.put("pageSize", pi.getPageSize());
			pageConfig.put("totalPage", pi.getPages());
			pageConfig.put("totalCount", pi.getTotal());
		}

		dataMap.put("pageConfig", pageConfig);
		dataMap.put("dataList", list);

		jsonMap.put("data", dataMap);
		jsonMap.put("status", status);
		jsonMap.put("message", message);

		return jsonMap;
	}
	/**
	 * 包装带分页列表的数据成为Map结构
	 * 
	 * @param list
	 * @param status
	 * @param message
	 * @return Map<String, Object>
	 */
	/*public static Map<String, Object> wrapPage(com.iflytek.util.PageInfo<Object> pi, String status, String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		jsonMap.put("data", getDataMap(pi));
		jsonMap.put("status", status);
		jsonMap.put("message", message);
		return jsonMap;
	}*/
	
	/*private static  Map<?, ?> getDataMap(com.iflytek.util.PageInfo<Object> pi){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> pageConfig = new HashMap<String, Object>();
		pageConfig.put("page", pi.getPage());
		pageConfig.put("pageSize", pi.getPageSize());
		pageConfig.put("totalPage", pi.getTotalPage());
		pageConfig.put("totalCount", pi.getTotalCount());
		dataMap.put("pageConfig", pageConfig);
		dataMap.put("dataList", pi.getDataList());
		return dataMap;
	}*/
	
	/**
	 * 封装没有分页的list
	 * 
	 * @param list
	 * @param status
	 * @param message
	 * @return
	 */
	public static Map<String, Object> wrapListNoPage(List<?> list, String status, String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (list != null && !list.isEmpty()) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("dataList", list);
			jsonMap.put("data", data);
		}
		jsonMap.put("status", status);
		jsonMap.put("message", message);

		return jsonMap;
	}

	/**
	 * 包装普通Map响应
	 * 
	 * @param data
	 * @param status
	 * @param message
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> wrapMap(Map<?, ?> data, String status, String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("data", data);
		jsonMap.put("status", status);
		jsonMap.put("message", message);
		jsonMap.put("location", null);
		
		return jsonMap;
	}

	/**
	 * 包装普通Map响应
	 * 
	 * @param data
	 * @param status
	 * @param message
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> wrapObject(Object data, String status, String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("data", data);
		jsonMap.put("status", status);
		jsonMap.put("message", message);
		jsonMap.put("location", null);
		
		return jsonMap;
	}
	
	public static Map<String, Object> wrapSuccess(String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("data", new HashMap<String, Object>());
		jsonMap.put("status", "success");
		jsonMap.put("message", message);
		jsonMap.put("location", null);
		
		return jsonMap;
	}
	public static Map<String, Object> wrapLogin(String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("data", new HashMap<String, Object>());
		jsonMap.put("status", "login");
		jsonMap.put("message", message);
		jsonMap.put("location", null);
		
		return jsonMap;
	}

	
	public static Map<String, Object> wrapObjSuccess(Object data,String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("data", data);
		jsonMap.put("status", "success");
		jsonMap.put("message", message);
		jsonMap.put("location", null);
		
		return jsonMap;
	}
	
	/**
	 * 返回异常信息
	 * @author zhangh
	 * @date 2018年7月18日上午10:18:16
	 * @param message
	 * @param location
	 * @return
	 */
	public static Map<String, Object> wrapErr(String message,String location) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("data", new HashMap<String, Object>());
		jsonMap.put("status", "error");
		jsonMap.put("message", message);
		jsonMap.put("location", location);
		return jsonMap;
	}
	
	public static Map<String, Object> wrapErr(String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("data", new HashMap<String, Object>());
		jsonMap.put("status", "error");
		jsonMap.put("message", message);
		jsonMap.put("location", null);
		return jsonMap;
	}
}