package com.gdtel.eshore.androidframework.common.base;

import android.os.Handler;
import android.os.Message;

public class BaseHander extends Handler{

	@Override
	public void dispatchMessage(Message msg) {

		super.dispatchMessage(msg);
	}

//	@Override
//	public String getMessageName(Message message) {
//
//		return super.getMessageName(message);
//	}

	@Override
	public void handleMessage(Message msg) {

		super.handleMessage(msg);
	}

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {

		return super.sendMessageAtTime(msg, uptimeMillis);
	}

}
