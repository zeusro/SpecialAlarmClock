package com.eshore.yourapp.activity.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gdtel.eshore.androidframework.common.base.BaseActivity;
import com.gdtel.eshore.androidframework.common.base.TaskCallBack;
import com.gdtel.eshore.androidframework.common.entity.LoginResult;
import com.gdtel.eshore.anroidframework.R;

public class LoginActivity extends BaseActivity implements TaskCallBack<LoginResult> {
	private EditText account , password;
	private Button loginBtn ;
	private TextView title;
	public static final int LOGIN_METHOD_CODE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.login_layout);
		super.onCreate(savedInstanceState);
		initView();
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
		account = (EditText)findViewById(R.id.account);
		password = (EditText)findViewById(R.id.password);
		loginBtn = (Button)findViewById(R.id.loginBtn);
		title = (TextView)findViewById(R.id.title);
		if (title!=null) {
			title.setText("登录");
		}
		loginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		if (id == R.id.loginBtn) {
			if (TextUtils.isEmpty(account.getText())||TextUtils.isEmpty(password.getText())) {
				showAlertDialog("登录提示", "用户账号或者密码不能为空！");
			}else {
//			    showDialog("正在登录，请耐心等待...");
				startActivity(MainActivity.class);
				finish();
				//登录
//				new LoginTask(this, this,LOGIN_METHOD_CODE).execute(account.getText().toString(),password.getText().toString());
			}
		} else {
		}
		super.onClick(v);
	}

	/**
	 * 返回结果处理
	 * {@inheritDoc}
	 */
	@Override
	public void callBackResult(LoginResult t) {
		dismissDialog();//关闭
//		showAlertDialog(title, message);//错误提示
		startActivity(MainActivity.class);
		finish();
	}

	/**
	 * 异常处理
	 * {@inheritDoc}
	 */
	@Override
	public void callbackError(CallBackError error) {

		dismissDialog();
//		showAlertDialog(title, message);
	}

	@Override
	public void initData() {

		super.initData();
	}

	@Override
	public void callBackResult(LoginResult t, int code) {

		dismissDialog();
		switch (code) {
		case LOGIN_METHOD_CODE:
			
			break;

		default:
			break;
		}
	}

}
