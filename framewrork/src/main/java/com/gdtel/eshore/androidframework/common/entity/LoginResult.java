package com.gdtel.eshore.androidframework.common.entity;

import java.io.Serializable;

public class LoginResult extends BaseResult implements Serializable{

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	
	public String userId ;//用户标识
	public String userName ;//用户名称
	public String userPasswd ;//用户密码
	
}
