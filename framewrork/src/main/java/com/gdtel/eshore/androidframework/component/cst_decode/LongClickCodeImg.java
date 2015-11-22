/**
 * Copyright  2015
 * All right reserved
  @Name: LongClickCodeImg.java 
  @Author: Wudl
  @Date: 2015年11月13日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.component.cst_decode;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import com.gdtel.eshore.androidframework.common.util.PhoneUtils;
import com.gdtel.eshore.androidframework.component.qr_codescan.camera.CameraManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Copyright  2015
 * All right reserved
  @Name: LongClickCodeImg.java 
  @Author: Wudl
  @Date: 2015年11月13日 
  @Description: 
 */
public class LongClickCodeImg extends ImageView 
	implements OnGestureListener{
	
	private final String TAG = "LongClickCodeImg";
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private CodeImgActHandler handler;
	private DecodeImgActivity activity;
	
	private float event_x,event_y;
	private int img_width,img_height;
	private boolean isDrawn = true;//是否已经截取了二维码图片
	private boolean isClickCode = false;//是否在二维码范围内
	private final int CODE_IMG_WIDTH = 250, CODE_IMG_HEIGHT = 250;
	
	private Bitmap srcImg,cropCodeImg;
	
	private GestureDetector gestureDor;
	
	private OnTouchListener touListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			
			return gestureDor.onTouchEvent(event);
		}
	};

	/**
	 * @param context
	 */
	public LongClickCodeImg(Context context) {
		super(context);

		Log.e(TAG, "super(context);");
		this.activity = (DecodeImgActivity) context;
		srcImg = ((BitmapDrawable)this.getDrawable()).getBitmap();
		img_width = srcImg.getWidth();
		img_height = srcImg.getHeight();
		gestureDor = new GestureDetector(getContext(), this);
		this.setOnTouchListener(touListener);
	}

	@SuppressLint("NewApi")
	public LongClickCodeImg(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		Log.e(TAG, "super(context, attrs, defStyleAttr, defStyleRes);");

		this.activity = (DecodeImgActivity) context;
		srcImg = ((BitmapDrawable)this.getDrawable()).getBitmap();
		img_width = srcImg.getWidth();
		img_height = srcImg.getHeight();
		gestureDor = new GestureDetector(getContext(), this);
		this.setOnTouchListener(touListener);
	}

	public LongClickCodeImg(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Log.e(TAG, "super(context, attrs, defStyleAttr);");

		this.activity = (DecodeImgActivity) context;
		srcImg = ((BitmapDrawable)this.getDrawable()).getBitmap();
		img_width = srcImg.getWidth();
		img_height = srcImg.getHeight();
		gestureDor = new GestureDetector(getContext(), this);
		this.setOnTouchListener(touListener);
	}

	public LongClickCodeImg(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.e(TAG, "super(context, attrs);");

		this.activity = (DecodeImgActivity) context;
		srcImg = ((BitmapDrawable)this.getDrawable()).getBitmap();
		img_width = srcImg.getWidth();
		img_height = srcImg.getHeight();
		gestureDor = new GestureDetector(getContext(), this);
		this.setOnTouchListener(touListener);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {

		event_x = e.getX();
		event_y = e.getY();
		isDrawn = false;
		postInvalidate();
		if (event_x > 0 && event_x < img_width / 3) {//x坐标为图片宽度的1/3
			if (event_y > img_height*2/3  && event_y < img_height) {//y坐标为图片长度1/3到图片总长之间
				isClickCode = true;
			}else {
				isClickCode = false;
			}
		}else {
			isClickCode = false;
		}
		
	}
	
	private void scanCodeImg(){
		CodeImgUtil imgUtil = new CodeImgUtil();
		byte[] data = Bitmap2Bytes(cropCodeImg);
		imgUtil.setInfo(data, img_width, img_height);
		if (handler == null) {
			handler = new CodeImgActHandler((DecodeImgActivity)getContext(), decodeFormats,
					characterSet,imgUtil);
			this.activity.setHandler(handler);
		}
	}

	private byte[] Bitmap2Bytes(Bitmap bm){  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);    
	    return baos.toByteArray();  
	   }  
	
	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		if (!isDrawn) {
			cropCodeImg = cropCodeImg(srcImg);
			if (cropCodeImg != null) {
				this.setImageBitmap(cropCodeImg);
				isDrawn = true;
			}
			if (isClickCode) {
				scanCodeImg();
			}
		}
	}
	
	private Bitmap cropCodeImg(Bitmap bitmap){
		int top,right,bottom;
		int left = PhoneUtils.convertDIP2PX(getContext(), 5);
		if (img_width > 0) {
			right = img_height / 3;
		}else {
			right = 0;
		}
		if (img_height > 0) {
			top = img_height*2/3;
			bottom = img_height;
		}else {
			top = 0;
			bottom = 0;
		}
		
		//生成空白Bitmap
		Bitmap resultImg = Bitmap.createBitmap
				(CODE_IMG_WIDTH, CODE_IMG_HEIGHT, Config.ARGB_4444);
		//使用空白图片生成画布
		Canvas canvas = new Canvas(resultImg);
		Rect srcRect = new Rect(left, top, right, bottom);
		Rect dstRect = new Rect(0, 0, CODE_IMG_WIDTH, CODE_IMG_HEIGHT);
		canvas.drawBitmap(bitmap, srcRect, dstRect, null);
		srcRect = null;
		dstRect = null;
		canvas = null;
		return resultImg;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onShowPress(android.view.MotionEvent)
	 */
	@Override
	public void onShowPress(MotionEvent e) {

		
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}
	
	

}
