/**
 * Copyright  2015
 * All right reserved
  @Name: CodeImgTestAct.java 
  @Author: Wudl
  @Date: 2015年11月16日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.component.cst_decode;

import android.os.Bundle;
import android.os.Handler;

import com.gdtel.eshore.androidframework.common.base.BaseActivity;
import com.gdtel.eshore.androidframework.component.qr_codescan.camera.CameraManager;
import com.google.zxing.Result;

/**
 * Copyright 2015 All right reserved
 * 
 * @Name: CodeImgTestAct.java
 * @Author: Wudl
 * @Date: 2015年11月16日
 * @Description:
 */
public class DecodeImgActivity extends BaseActivity {

	private LongClickCodeImg img;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		CameraManager.init(getApplication());
//		设置以下布局仅用于测试效果，放开可以看到效果
//		setContentView(R.layout.code_img_layout);
//		img = (LongClickCodeImg) findViewById(R.id.code_img);
	}

	/**
	 * 
	  @Type:void
	  @Author: Wudl 
	  @Date: 2015年11月17日 
	  @Description: 二维码解码后的处理结果
	 */
	public void handleDecode(Result result) {
		/**
		 * 以下的场景为弹出地址并用浏览器打开，在具体项目中具体实现
		 */
//		Toast.makeText(this, result.getText(), 100).show();
//		Intent intent = new Intent();
//		intent.setAction("android.intent.action.VIEW");
//		Uri content_url = Uri.parse(result.getText());
//		intent.setData(content_url);
//		startActivity(intent);
	}

	/**
	 * 
	  @Type:Handler
	  @Author: Wudl 
	  @Date: 2015年11月17日 
	  @Description: 返回handler
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * 
	  @Type:void
	  @Author: Wudl 
	  @Date: 2015年11月17日 
	  @Description: 设置handler，用于消息返回处理
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
