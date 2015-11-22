/**
 * Copyright  2015
 * All right reserved
  @Name: CodeImgUtil.java 
  @Author: Wudl
  @Date: 2015年11月16日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.component.cst_decode;

import java.util.Hashtable;

import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.gdtel.eshore.androidframework.component.qr_codescan.camera.CameraManager;
import com.gdtel.eshore.androidframework.component.qr_codescan.camera.PlanarYUVLuminanceSource;
import com.gdtel.eshore.androidframework.component.qr_codescan.decoding.RGBLuminanceSource;
import com.gdtel.eshore.anroidframework.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * Copyright  2015
 * All right reserved
  @Name: CodeImgUtil.java 
  @Author: Wudl
  @Date: 2015年11月16日 
  @Description: 
 */
public class CodeImgUtil {
	
	private final MultiFormatReader multiFormatReader;
	private byte[] data;
	private int width,height;
	
	public CodeImgUtil() {
		super();
		this.multiFormatReader = new MultiFormatReader();
	}

	public Result decode(byte[] data, int width, int height) {
		Result rawResult = null;
		//modify here
	    byte[] rotatedData = new byte[data.length];
	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++)
	            rotatedData[x * height + height - y - 1] = data[x + y * width];
	    }
	    int tmp = width; // Here we are swapping, that's the difference to #11
	    width = height;
	    height = tmp;
	    
	    PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    try {
	      rawResult = multiFormatReader.decodeWithState(bitmap);
	    } catch (ReaderException re) {
	      // continue
	    } finally {
	      multiFormatReader.reset();
	    }
	    return rawResult;
	}
	
	public void setInfo(byte[] data,int width,int height){
		this.data = data;
		this.width = width;
		this.height = height;
	}
	
	public byte[] getData(){
		return this.data;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	
	//解析二维码图片,返回结果封装在Result对象中
		public Result  decodeBitmap(DecodeImgActivity activity,
				byte[] data, int width, int height){
//			long start = System.currentTimeMillis();
			//解析转换类型UTF-8
			Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
			hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
			BitmapFactory.Options options = new BitmapFactory.Options(); 
			//把inJustDecodeBounds设为true不会返回一个Bitmap，它仅仅会把它的宽，高返回
			options.inJustDecodeBounds = true;
			//此时的bitmap是null，可以设置options.outWidth 和 options.outHeight为我们想要的宽和高
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options); 
			options.inSampleSize = options.outHeight / 300;
			if(options.inSampleSize <= 0){
				options.inSampleSize = 1; //防止其值小于或等于0
			}
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options); 
			//新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
			RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
			//将图片转换成二进制图片
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
			//初始化解析对象
			QRCodeReader reader = new QRCodeReader();
			//开始解析
			Result result = null;
			try {
				result = reader.decode(binaryBitmap, hints);
			} catch (Exception e) {

			}
			
			if (result != null) {
				Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, result);
			    Bundle bundle = new Bundle();
			    bundle.putParcelable(DecodeImgThread.BARCODE_BITMAP, bitmap);
			    message.setData(bundle);
			      //Log.d(TAG, "Sending decode succeeded message...");
			    message.sendToTarget();
			} else {
			      Message message = Message.obtain(activity.getHandler(), R.id.decode_failed);
			      message.sendToTarget();
			    }
			
			bitmap = null;
			rgbLuminanceSource = null;
			binaryBitmap = null;
			reader = null;
			System.gc();
			
			return result;
		}

}
