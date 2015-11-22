package com.eshore.yourapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gdtel.eshore.androidframework.common.base.BaseActivity;
import com.gdtel.eshore.androidframework.common.util.AppConstant;
import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
import com.gdtel.eshore.anroidframework.R;

public class MyAttentionActivity extends BaseActivity {
	private String name ;
	private TextView contend ;
	private static final String TAG = "MyAttentionActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		name = intent.getStringExtra(AppConstant.APP_ITEM_TITLE);
		DebugLog.d(TAG, name);
		setContentView(R.layout.activity_myattention);
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void initData() {
		
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public void initView() {
		contend = (TextView) findViewById(R.id.contend);
		if (!TextUtils.isEmpty(name)&&title!=null) {
			contend.setText(name);
			title.setText(name);
		}
		super.initView();
	}

	@Override
	public void onClick(View v) {

		
		super.onClick(v);
	}

}
