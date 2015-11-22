package com.gdtel.eshore.androidframework.common.task;

import android.app.ProgressDialog;
import android.content.Context;

import com.gdtel.eshore.androidframework.common.base.BaseAsyncTask;

public class TestTasck extends BaseAsyncTask<String, Integer, String> {

	public TestTasck(Context ctx, ProgressDialog dialog) {
		super(ctx, dialog);
	}
}
