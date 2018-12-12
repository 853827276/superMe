package com.hengzhang.springboot.util;

public class Column {
	private String title;
	private String key;
	private String style;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public Column() {
		super();
	}
	public Column(String title, String key) {
		super();
		this.title = title;
		this.key = key;
	}
	public Column(String title, String key, String style) {
		super();
		this.title = title;
		this.key = key;
		this.style = style;
	}
}
