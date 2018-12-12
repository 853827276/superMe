package com.hengzhang.springboot.util;

/**
 * @author weiliu36
 * @date 2018-08-31下午3:54:21
 */
public enum CustomEnum {
	//系统支持的文件格式  1:视频;0:图片
	SUPPORTED_FILE_FORMAT_IMAGE(0,"图片(png,jpg)"),
	SUPPORTED_FILE_FORMAT_VIDEO(1,"视频(avi,mp4)"),
	SUPPORTED_FILE_FORMAT_NONE(-1,"未知"),;
	
	private Integer code;
	private String msg;

	CustomEnum(Integer code, String msg) {
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
