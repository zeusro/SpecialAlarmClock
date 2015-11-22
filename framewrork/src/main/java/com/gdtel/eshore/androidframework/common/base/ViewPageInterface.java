/**
 * Copyright  2015
 * All right reserved
  @Name: ViewPageInterface.java 
  @Author: Wudl
  @Date: 2015年8月5日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.common.base;

import java.util.List;

import android.support.v4.app.Fragment;

public interface ViewPageInterface<T> {
	
	/**
	 * 
	 * Copyright  2015
	 * All right reserved
	  @Type:void
	  @Author: Wudl 
	  @Date: 2015年8月5日 
	  @Description: 初始化ViewPage
	 */
	public void initViewPage(List<T> lisData);
	
	public List<T> initPageLayout();
	
	public void setTaskCallBack(TaskCallBack<Integer> callBack);

}
