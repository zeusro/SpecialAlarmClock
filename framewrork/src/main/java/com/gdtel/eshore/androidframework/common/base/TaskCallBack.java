package com.gdtel.eshore.androidframework.common.base;


public interface TaskCallBack<T> {
		public enum CallBackError{
			CONNECTION_TIMEOUT,	//链接超时
			OTHER_EXCEPTION; //其他错误
		}
		public void callBackResult(T t);
		public void callBackResult(T t , int code);
		public void callbackError(TaskCallBack.CallBackError error);
		
	}