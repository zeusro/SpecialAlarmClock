/**
 * Copyright  2015
 * All right reserved
  @Name: CodeImgHandler.java 
  @Author: Wudl
  @Date: 2015年11月17日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.component.cst_decode;


import java.util.Hashtable;

import com.gdtel.eshore.anroidframework.R;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Copyright  2015
 * All right reserved
  @Name: CodeImgHandler.java 
  @Author: Wudl
  @Date: 2015年11月17日 
  @Description: 
 */
public class CodeImgHandler extends Handler {
	
	private static final String TAG = CodeImgHandler.class.getSimpleName();
	
	private Activity activity;
	private final MultiFormatReader multiFormatReader;
	public CodeImgHandler(DecodeImgActivity activity, Hashtable<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
	    multiFormatReader.setHints(hints);
	    this.activity = activity;
	}
	@Override
	public void handleMessage(Message msg) {

		if (msg.what == R.id.decode) {
			CodeImgUtil imgUtil = new CodeImgUtil();
			imgUtil.decodeBitmap((DecodeImgActivity)activity,(byte[])msg.obj,msg.arg1,msg.arg2);
		} else if (msg.what == R.id.quit) {
			Looper.myLooper().quit();
		}
	}
	
	

}
