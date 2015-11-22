package com.gdtel.eshore.androidframework.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
/**
 * 基础异步类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2014-2-13]
 * @param <T>
 * @param <T>
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BaseAsyncTask<T,K,M> extends AsyncTask<T , K, M> {
	private Context ctx;
	private ProgressDialog dialog;
	private boolean indeterminate = true;
	private boolean mOutside = false ;
	private boolean mCancelable = true ;
	private String title ;
	private String msg;
	protected int code;
	
	public boolean isIndeterminate() {
		return indeterminate;
	}

	public void setIndeterminate(boolean indeterminate) {
		this.indeterminate = indeterminate;
	}

	public boolean ismOutside() {
		return mOutside;
	}

	public void setmOutside(boolean mOutside) {
		this.mOutside = mOutside;
	}

	public boolean ismCancelable() {
		return mCancelable;
	}

	public void setmCancelable(boolean mCancelable) {
		this.mCancelable = mCancelable;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public BaseAsyncTask(Context ctx,
			ProgressDialog dialog) {
		super();
		this.ctx = ctx;
		this.dialog = dialog;
	}
	public BaseAsyncTask(Context ctx) {
		super();
		this.ctx = ctx;
	}
	public BaseAsyncTask(Context ctx,
			ProgressDialog dialog,int code) {
		super();
		this.ctx = ctx;
		this.dialog = dialog;
		this.code = code;
	}


	@Override
	protected void onCancelled() {

		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Object result) {
//		if (dialog != null) {
//			dialog.dismiss();
//		}
//		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
//		showDialog();
//		showDialog(true,false,true,null,null);
		super.onPreExecute();
	}
	/**
	 * 关闭提示框
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void dismissDialog(){
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	/**
	 * 显示提示框
	 * <功能详细描述>
	 * @param indeterminate
	 * @param mOutside
	 * @param mCancelable
	 * @param title
	 * @param msg
	 * @see [类、类#方法、类#成员]
	 */
	public void showDialog() {
		// boolean indeterminate,boolean mOutside , boolean mCancelable , String title , String msg
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}else {
			initDialog();
		}
	}
	/**
	 * 初始化对话框
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void initDialog() {

		dialog = ProgressDialog.show(ctx, 
				TextUtils.isEmpty(title)?"请稍后":title, TextUtils.isEmpty(msg)?"数据正在加载...":msg, indeterminate);
		dialog.setCanceledOnTouchOutside(mOutside);
		dialog.setCancelable(mCancelable);
		dialog.show();
	}

	@Override
	protected M doInBackground(T... params) {

		return null;
	}

	@Override
	protected void onProgressUpdate(K... values) {

		super.onProgressUpdate(values);
	}


}
