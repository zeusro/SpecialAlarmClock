/**
 * Copyright  2015
 * All right reserved
  @Name: CodeImgAACtHandler.java 
  @Author: Wudl
  @Date: 2015年11月17日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.component.cst_decode;

import java.util.Vector;

import com.gdtel.eshore.androidframework.component.qr_codescan.camera.CameraManager;
import com.gdtel.eshore.anroidframework.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Copyright  2015
 * All right reserved
  @Name: CodeImgAACtHandler.java 
  @Author: Wudl
  @Date: 2015年11月17日 
  @Description: 
 */
public class CodeImgActHandler extends Handler {
	
	private static final String TAG = CodeImgActHandler.class.getSimpleName();
	private final DecodeImgActivity activity;
	private final DecodeImgThread decodeThread;
	private CodeImgUtil util;
	public CodeImgActHandler(DecodeImgActivity activity, Vector<BarcodeFormat> decodeFormats,
		      String characterSet, CodeImgUtil util) {
		this.activity = activity;
		this.util = util;
		decodeThread = new DecodeImgThread(activity, decodeFormats, characterSet);
		decodeThread.start();
		sendHanlder();
	}
	
	
	public CodeImgUtil getUtil() {
		return util;
	}


	public void setUtil(CodeImgUtil util) {
		this.util = util;
	}


	private void sendHanlder(){
		Message message = decodeThread.getHandler().obtainMessage(R.id.decode, this.util.getWidth(),
		this.util.getHeight(), this.util.getData());
		message.sendToTarget();
	}


	@Override
	public void handleMessage(Message msg) {

		if (msg.what == R.id.decode_succeeded) {
			Log.d(TAG, "Got decode succeeded message");
			Bundle bundle = msg.getData();
	        Bitmap barcode = bundle == null ? null :
	            (Bitmap) bundle.getParcelable(DecodeImgThread.BARCODE_BITMAP);
			activity.handleDecode((Result) msg.obj);
			if (barcode != null) {
				barcode = null;
			}
			if (bundle != null) {
				bundle = null;
			}
		}
	}

	public void quitSynchronously() {
	    CameraManager.get().stopPreview();
	    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
	    quit.sendToTarget();
	    try {
	      decodeThread.join();
	    } catch (InterruptedException e) {
	      // continue
	    }

	    // Be absolutely sure we don't send any queued up messages
	    removeMessages(R.id.decode_succeeded);
	    removeMessages(R.id.decode_failed);
	  }
	
}
