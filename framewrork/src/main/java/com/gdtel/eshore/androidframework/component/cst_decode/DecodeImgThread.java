/**
 * Copyright  2015
 * All right reserved
  @Name: DecodeImgThread.java 
  @Author: Wudl
  @Date: 2015年11月17日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.component.cst_decode;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

/**
 * Copyright  2015
 * All right reserved
  @Name: DecodeImgThread.java 
  @Author: Wudl
  @Date: 2015年11月17日 
  @Description: 
 */
public class DecodeImgThread extends Thread {
	
	public static final String BARCODE_BITMAP = "barcode_bitmap";
	
	private final Hashtable<DecodeHintType, Object> hints;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	private Activity activity;
	public DecodeImgThread(DecodeImgActivity activity, Vector<BarcodeFormat> decodeFormats,
            String characterSet) {
		this.activity = activity;
	    handlerInitLatch = new CountDownLatch(1);

	    hints = new Hashtable<DecodeHintType, Object>(3);

	    if (decodeFormats == null || decodeFormats.isEmpty()) {
	    	 decodeFormats = new Vector<BarcodeFormat>();
	    	 decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
	    	 decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
	    	 decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
	    }
	    
	    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

	    if (characterSet != null) {
	      hints.put(DecodeHintType.CHARACTER_SET, characterSet);
	    }
	}
	
	Handler getHandler() {
	    try {
	      handlerInitLatch.await();
	    } catch (InterruptedException ie) {
	      // continue?
	    }
	    return handler;
	  }

	@Override
	public void run() {

		Looper.prepare();
		handler = new CodeImgHandler((DecodeImgActivity)activity, hints);
		handlerInitLatch.countDown();
	    Looper.loop();
	}
	
	
	

}
