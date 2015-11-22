package com.gdtel.eshore.androidframework.common.task;

import android.content.Context;
import android.os.AsyncTask;

import com.gdtel.eshore.androidframework.common.base.TaskCallBack;
import com.gdtel.eshore.androidframework.common.entity.LoginResult;
import com.gdtel.eshore.androidframework.common.net.http.MethodParameters;

public class LoginTask extends AsyncTask<String, Void, LoginResult> {
	private Context context;
	private int code;
	private TaskCallBack<LoginResult> taskCallBack;

	public LoginTask(Context context, TaskCallBack<LoginResult> taskCallBack) {
		this.context = context;
		this.taskCallBack = taskCallBack;
	}

	public LoginTask(Context context, TaskCallBack<LoginResult> taskCallBack,int code) {
		this.context = context;
		this.taskCallBack = taskCallBack;
		this.code = code;
	}

	@Override
	protected LoginResult doInBackground(String... param) {
		//TODO	获取数据
		return MethodParameters.login(context, param[0], param[1]);
	}

	@Override
	protected void onPostExecute(LoginResult result) {
//		super.onPostExecute(result);
		if(taskCallBack!=null){
			taskCallBack.callBackResult(result,code);
		}
	}

}