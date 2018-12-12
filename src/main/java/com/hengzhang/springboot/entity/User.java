package com.hengzhang.springboot.entity;

import com.hengzhang.springboot.common.BaseEntity;

public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 765393883806061081L;
	
	private String name;
	private String password;
	private String firstLogin;
	
	
	public String getFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User() {
		super();
	}
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	
}
