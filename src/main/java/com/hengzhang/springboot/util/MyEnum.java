package com.hengzhang.springboot.util;

/**
 * 系统枚举类
 * 
 * @author zhangh
 * @date 2018年9月6日下午2:34:37
 */
public enum MyEnum {
	
	PAGE_INIT_VALUE(1,"page 初始值"),
	PAGESIZE_INIT_VALUE(20,"pagesize 初始值"),
	USER_FIRST_LOGIN(1,"0"),//0 未登录
	ES_INDEX_NAME(1,"book"),
	ES_INDEX_TYPE(1,"booktype"),
	USER_COOK_KEY(1,"iflytek_2018_cookie_key_");
	
	private Integer code;
	private String msg;

	MyEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
