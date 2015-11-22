package com.gdtel.eshore.androidframework.common.util.photo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class PicUtils {
    private static final String TAG = "PicUtils";
    /**
     * 截屏
     * <功能详细描述>
     * @param mActivity 被截屏界面
     * @param filePath  保存路径
     * @see [类、类#方法、类#成员]
     */
	public static boolean GetandSaveCurrentImage(Activity mActivity,String filePath){
	    boolean res = true;
	    if(mActivity == null || TextUtils.isEmpty(filePath)) {
	        res = false;
	        DebugLog.d(TAG, "被截屏界面或则保存路径不能为空 ：  mActivity="+mActivity+" filePath="+filePath);
	    }
	    //1.构建Bitmap   
	    WindowManager windowManager = mActivity.getWindowManager();   
	    Display display = windowManager.getDefaultDisplay();   
	    int w = display.getWidth();   
	    int h = display.getHeight();   
	    Bitmap Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );       
	    //2.获取屏幕   
	    View decorview = mActivity.getWindow().getDecorView();    
	    decorview.setDrawingCacheEnabled(true);    
	    Bmp = decorview.getDrawingCache();    
	   //3.保存Bitmap   
	    saveImage(Bmp,filePath,100);
	   return res;
	}
	/**
	 * 保存图片
	 * <功能详细描述>
	 * @param Bmp 图片
	 * @param filePath 路径
	 * @param quality 质量百分比
	 * @see [类、类#方法、类#成员]
	 */
	public static void saveImage(Bitmap Bmp,String filePath,int quality){
	    BufferedOutputStream bos = null;
	    if(TextUtils.isEmpty(filePath)) return ;
		try {
			File dir = new File(filePath);
			if(!dir.exists())
				dir.mkdirs();
	    	File path = new File(filePath);// 给新照的照片文件命名
//	    	path.createNewFile();
			bos = new BufferedOutputStream(
					new FileOutputStream(path));
			/* 采用压缩转档方法 */
//			int tt = (int)(path.length()/1000)/10;
//			Bmp = compressImage(Bmp);
			Bmp.compress(Bitmap.CompressFormat.JPEG, quality, bos);//0 - 100  100表示不压缩
			/* 调用flush()方法，更新BufferStream */
			bos.flush();
			/* 结束OutputStream */
			DebugLog.i(TAG,"直接得到图片大小  path.length():"+(path.length())+" ");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		    try {
		        if(bos!=null){
		            bos.close();
		        }
            } catch (IOException ex) {

                ex.printStackTrace();
            }
		}
	}
	/**
	 * 质量压缩方法
	 * <功能详细描述>
	 * @param image
	 * @param quality
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static Bitmap compressImage(Bitmap image,int quality) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        int len = baos.toByteArray().length;
        while ( len / 1024>60) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            options -= 10;//每次都减少10  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            if (options<=0) break;
            Log.i("第二次直接得到图片大小",len+" path.length() options="+options);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
	/**
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param Bmp
	 * @param filePath
	 * @param path
	 * @see [类、类#方法、类#成员]
	 */
	public static void saveSecond(Bitmap Bmp,String filePath , File path){
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		opts.inSampleSize = (int)(path.length()/1000)/10;
		opts.inJustDecodeBounds = false;
		Bmp = BitmapFactory.decodeFile(filePath, opts);
		
		try {
//			File dir = new File(RegisterInfoAct.PHOTO_DIR);
//		if(!dir.exists())
//			dir.mkdirs();
			if (path.exists()) {
				path.delete();
			}
    	path = new File(filePath);// 给新照的照片文件命名
//    	path.createNewFile();
		BufferedOutputStream bos;
		
			bos = new BufferedOutputStream(
					new FileOutputStream(path));
		
		/* 采用压缩转档方法 */
		Bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		/* 调用flush()方法，更新BufferStream */
		bos.flush();
		/* 结束OutputStream */
		bos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("第二次直接得到图片大小",(int)(path.length()/1000)+"path.length()");
	}
	/**
	 * 缩放
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	/**
	 * 压缩尺寸
	 * <功能详细描述>
	 * @param filePath 图片路径
	 * @param reqWidth 宽
	 * @param reqHeight 高
	 * @return 压缩后的图片
	 * @see [类、类#方法、类#成员]
	 */
	public static Bitmap getSmallBitmap(String filePath,
			int reqWidth, int reqHeight) {
	    if(TextUtils.isEmpty(filePath)) return null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
}
