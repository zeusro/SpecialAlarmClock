package com.gdtel.eshore.androidframework.common.base;

import android.os.Message;

public interface BaseLogic {
	/**
	 * 初始view
	 */
	public void initView();

	/**
	 * 消息处理
	 * 
	 * @param msg
	 */
	public void messageHandler(Message msg);
	/**
	 * 初始数据
	 */
	public void initData() ;
}
