package com.eshore.yourapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdtel.eshore.androidframework.common.base.BaseActivity;
import com.gdtel.eshore.androidframework.common.entity.MoreItemEntity;
import com.gdtel.eshore.androidframework.common.util.AppConstant;
import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
import com.gdtel.eshore.androidframework.ui.adapter.MoreItemAdapter;
import com.gdtel.eshore.anroidframework.R;

public class PersonalInformationActivity extends BaseActivity {
	private String name ;
	private TextView contend ;
	private RelativeLayout left_layout ,right_layout;
	private Button right_btn;
	private ListView listView;
	private List<MoreItemEntity> items = new ArrayList<MoreItemEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initData();
		Intent intent = getIntent();
		name = intent.getStringExtra(AppConstant.APP_ITEM_TITLE);
		DebugLog.d(TAG, name);
		setContentView(R.layout.activity_personalinformation);
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public void initData() {
		MoreItemEntity itemEntity1 = new MoreItemEntity();
		itemEntity1.setItemName("itemName1");
		itemEntity1.setDrawable(null);
		items.add(itemEntity1);
		MoreItemEntity itemEntity2 = new MoreItemEntity();
		itemEntity2.setItemName("itemName2");
		itemEntity2.setDrawable(null);
		items.add(itemEntity2);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected void onResume() {

		
		super.onResume();
	}

	@Override
	public void initView() {
		contend = (TextView) findViewById(R.id.contend);
		left_layout = (RelativeLayout) findViewById(R.id.left_layout);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new MoreItemAdapter(this, items));
		right_layout = (RelativeLayout) findViewById(R.id.right_layout);
		right_layout.setOnClickListener(this);
		right_btn = (Button) findViewById(R.id.right_btn);
		right_btn.setVisibility(View.VISIBLE);
		right_btn.setOnClickListener(this);
		right_btn.setText("更多");
		if (!TextUtils.isEmpty(name)&&title!=null) {
			contend.setText(name);
			title.setText(name);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (right_layout.isShown()) {
					rightLayoutGone();
				}else {
					right_layout.setVisibility(View.VISIBLE);
				}
			}

		});
		super.initView();
	}
	/**
	 * 隐藏右边
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void rightLayoutGone() {

		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) left_layout
				.getLayoutParams();;
		layoutParams.leftMargin = 0;
		left_layout.setLayoutParams(layoutParams);
		right_layout.setVisibility(View.GONE);
	}
	/**
	 * 显示右边
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void rightLayoutVisible() {

		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) left_layout
				.getLayoutParams();;
				layoutParams.leftMargin = -300;
				left_layout.setLayoutParams(layoutParams);
				right_layout.setVisibility(View.VISIBLE);
	}
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		if (id == R.id.right_btn) {
			if (right_layout!=null&&right_layout.isShown()) {
				rightLayoutGone();
			}else {
				rightLayoutVisible();
			}
		} else if (id == R.id.right_layout) {
			if (right_layout!=null&&right_layout.isShown()) {
				rightLayoutGone();
			}
		} else {
		}
		super.onClick(v);
	}

}
