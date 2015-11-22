package com.gdtel.eshore.androidframework.common.util.terminalability;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 手机通用工具类
 * <p>
 * 用这个工具类可以实现打电话、发短信、录音、拍照和录像等功能
 * 
 * @author  dengfy
 * @version  [版本号, 2014-12-24]
 */
public class MobileUtils {

	private static final String TAG = "MobileUtils";
	
	public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
	    
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int RECORD_SOUND_ACTIVITY_REQUEST_CODE = 300;
    
    public static Uri fileUri;
    
	/**
	 * 对指定号码拨号<br />
	 * 
	 * 拨号需要添加拨号权限：android.permission.CALL_PHONE
	 * 
	 * @param context
	 * @param num 电话号码
	 * @return true表示手机中存在拨号app，可以进行拨号,false表示没有,无法拨号
	 */
	public static boolean makeCall(Context context, String num) {
		checkContext(context);
		
		Uri uri = Uri.parse("tel:" + num); 
		Intent intent = new Intent(Intent.ACTION_CALL, uri); 
		if (context.getPackageManager().resolveActivity(intent, 0) != null) {
	    	context.startActivity(intent);
			return true;
		}
		return false;		
	}
	
	/**
	 * 发短信
	 * 
	 * @param context
	 * @param num 电话号码
	 * @param content 短信内容
	 * @return true表示手机中存在短信app，可以发送短信,false表示没有,无法发送短信
	 */
	public static boolean sendSms(Context context, String num, String content) {
		checkContext(context);
		
		Uri uri = Uri.parse("smsto:" + num);
	    Intent intent = new Intent( android.content.Intent.ACTION_SENDTO, uri);
	    intent.putExtra("sms_body", content);
	    
	    if (context.getPackageManager().resolveActivity(intent, 0) != null) {
	    	context.startActivity(intent);
			return true;
		}
		return false;    
	}
	
	/**
	 * 调用系统自带的音频录制app进行录音 <br/>
	 * 
	 * 调用此方法后，在onActivityResult（）方法中，获取Intent的Data，就是录制的音频对应的URI。
	 * <p>
	 * 注意：调用系统自带的录音app不能指定录音文件的保存路径，如果想保存在别的路径下，可以在onActivityResult（）方法中，
	 * 获取录音文件的URI，然后把它复制指定的路径下
	 * 
	 * @param activity
	 * @return true表示手机中存在音频录制app,false表示没有，无法调用系统自带的音频录制app进行录音
	 */
	public static boolean recordSound(Activity activity) {
		checkContext(activity);
		
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		
		if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
			activity.startActivityForResult(intent, RECORD_SOUND_ACTIVITY_REQUEST_CODE);
			return true;
		}
		return false;
	}
	
	/**
	 * 调用系统相机进行拍照
	 * 
	 * @param activity
	 * @param savePath 图片的保存路径
	 * @return true表示拍照成功，false表示失败（失败原因可能是sd卡没有挂载，或没有sd的访问权限）
	 */
	public static boolean captureImage(Activity activity, String savePath) {
		checkContext(activity);
		
		// 调用系统自带的相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, savePath);
        if (fileUri == null) {
			return false;
		}
        
        // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，
        //如果此处指定，则后来的data为null
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        
        return true;
	}
	
	/**
	 * 调用系统相机录像（保存格式为格式MP4）
	 * 
	 * @param activity
	 * @param savePath 视频的保存路径
	 * @return true表示录像成功，false表示失败（失败原因可能是sd卡没有挂载，或没有sd的访问权限）
	 */
	public static boolean captureVideo(Activity activity, String savePath) {
		checkContext(activity);
		
		// 调用系统相机摄像
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO, savePath);
        if (fileUri == null) {
			return false;
		}
      
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        activity.startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        
        return true;
	}
	
	/** 创建一个文件Uri来保存图片或视频 */
    private static Uri getOutputMediaFileUri(int type, String savePath) {
    	try {
			return Uri.fromFile(getOutputMediaFile(type, savePath));
		} catch (Exception e) {
			return null;
		}
    }

    /** 创建一个文件来保存图片或视频 */
    private static File getOutputMediaFile(int type, String savePath) {
        //检查SD卡是否存在
    	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    		Log.d(TAG, "SD卡不存在，请插入SD卡");
    		return null;
		}  	

        File mediaStorageDir = new File(savePath);
            
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.d(TAG, "创建目录失败, 检查是否有 WRITE_EXTERNAL_STORAGE 权限");
                return null;
            }
        }

        //创建图片或视频名字
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
        		Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }
        else {
            return null;
        }

        return mediaFile;
    }   
	
	/**
	 * 检查用户传进来的Context对象是否为空
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 */
	private static void checkContext(Context context) {
		if (context == null) {
			throw new NullPointerException("context 对象不能为空");
		}
	}
}
