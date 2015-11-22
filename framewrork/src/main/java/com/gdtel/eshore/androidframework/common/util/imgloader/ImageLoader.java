package com.gdtel.eshore.androidframework.common.util.imgloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gdtel.eshore.androidframework.common.base.TaskCallBack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	private int QEQUIRED_MEHHOD = 0;
	private  int REQUIRED_SIZE = 100;
	private MemoryCache memoryCache = new MemoryCache();
	private AbstractFileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;
	private ImageLoaderInterface imamageLoaderInterface;
	private static int fileSize = 0;  
	private static int downloadSize = 0; 
	private int progress;
	private TaskCallBack<String> imgCallBack; 
	private int code;
	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}
	
	public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache,TaskCallBack<String> imgCallBack,int code) {
		this.imgCallBack = imgCallBack;
		this.code = code;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		}else if (!isLoadOnlyFromCache){
			queuePhoto(url, imageView);
		}
	}
	
	public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		}else if (!isLoadOnlyFromCache){
			queuePhoto(url, imageView);
		}
	}
	
	/**
	 * 
	 * @param url
	 * @param imageView
	 * @param isLoadOnlyFromCache
	 * @param REQUIRED_SIZE
	 * @param QEQUIRED_MEHHOD 只能是0和1;
	 */
	public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache,int REQUIRED_SIZE,int QEQUIRED_MEHHOD) {
		this.QEQUIRED_MEHHOD = QEQUIRED_MEHHOD;
		this.REQUIRED_SIZE = REQUIRED_SIZE;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		}else if (!isLoadOnlyFromCache){
			queuePhoto(url, imageView);
		}
	}
	
	public void DisplayImage(String url, ImageView imageView,
			ImageLoaderInterface imamageLoaderInterface,
			boolean isLoadOnlyFromCache,int REQUIRED_SIZE,int QEQUIRED_MEHHOD) {
		this.QEQUIRED_MEHHOD = QEQUIRED_MEHHOD;
		this.REQUIRED_SIZE = REQUIRED_SIZE;
		this.imamageLoaderInterface = imamageLoaderInterface;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		}else if (!isLoadOnlyFromCache){
			queuePhoto(url, imageView);
		}
	}
	
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);
		
		Bitmap b = null;
		if (f != null && f.exists()){
			b = decodeFile(f);
		}
		if (b != null){
			return b;
		}
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			Log.i("LoaderImage", "下载图片的Url："+imageUrl);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			fileSize = conn.getContentLength();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			Log.e("", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
			return null;
		}
	}

	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			switch(QEQUIRED_MEHHOD){
			case 0:
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
				o2.inSampleSize = scale;
				break;
			case 1:
				o2.outWidth = REQUIRED_SIZE;
				o2.outHeight = height_tmp * REQUIRED_SIZE /width_tmp;
//				o2.inSampleSize = scale;
				break;
			}
			

			// decode with inSampleSize
			
			
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * ��ֹͼƬ��λ
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// ������UI�߳��и��½���
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			if (imamageLoaderInterface != null) {
				imamageLoaderInterface.imageDownloaded(
						photoToLoad.imageView,bitmap);
			}
			
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
				downloadSize += count; 
				progress = downloadSize*100/fileSize;  
				if (imgCallBack != null) {
					this.imgCallBack.callBackResult(String.valueOf(progress), code);
				}
				Log.e("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&", ":"+progress);
			}
		} catch (Exception ex) {
			Log.e("", "CopyStream catch Exception...");
		}
	}
	
}