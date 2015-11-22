package com.gdtel.eshore.androidframework.common.util;


import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


public class PhoneUtils {
	private static final String TAG = "PhoneUtil";
	/**
	 * 获取手机imsi码
	 * 国际移动用户识别码（IMSI） International Mobile Subscriber Identity 
	 * 国际上为唯一识别一个移动用户所分配的号码。
	 * IMSI共有15位，其结构如下： 
	 * MCC+MNC+MIN 
	 * MCC：Mobile Country Code，移动国家码，共3位，中国为460; 
	 * MNC:Mobile Network Code，移动网络码，共2位，联通CDMA系统使用03，一个典型的IMSI号码为460030912121001; 
	 * MIN共有10位，其结构如下： 09+M0M1M2M3+ABCD 
	 * 其中的M0M1M2M3和MDN号码中的H0H1H2H3可存在对应关系，ABCD四位为自由分配。 
	 */
	public static String getImsi(Context ctx) {
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephonyManager.getSubscriberId();
		return imsi;
//		return "123456";
	}
	
	
	/**
	 * 获取手机ESN
	 * CDMA手机机身号简称ESN-Electronic Serial Number的缩写。
	 * 它是一个32bits长度的参数，是手机的惟一标识。
	 * GSM手机是IMEI码(International Mobile Equipment Identity)，
	 * 国际移动身份码。
	 */
	public static String getEsn(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String esn = tm.getDeviceId();//DeviceId(IMEI)
		return esn;
	}
	
	/**
	 * 获取手机MODLE,手机型号
	 */
	public static String getModel() {
		String model = Build.MODEL;	
		//模拟器环境默认使用XT800
		if("sdk".equals(model)){
			model = "XT800";
		}
		return model;
	}
	
	/**
	 * 获取手机ROM
	 */
	public static String getRom() {
//		String PRODUCT = Build.PRODUCT; //如 titanium
//		String DEVICE = Build.DEVICE;//如 titanium	
		String DISPLAY = Build.DISPLAY;	//如 titanium-userdebug 2.1 TITA_K29_00.13.01I 173018 test-keys
//		String ID = Build.ID;//如 TITA_K29_00.13.01I	
//		String RELEASE = Build.VERSION.RELEASE;	//如 2.1
//		String INCREMENTAL = Build.VERSION.INCREMENTAL;	//如 173018
		return DISPLAY;
	}
	
	/**
	 * 判断sdCard 最小10M
	 * @param ctx
	 * @return 0-可用 1-sd卡空间不足 2-sd卡不存在
	 */
	@SuppressWarnings("deprecation")
    public static int SDCardAvailable(Context ctx){
		//判断sd卡
		int result = 0;
		boolean sdCardExit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (sdCardExit) {
			File sdPath = Environment.getExternalStorageDirectory();
			StatFs sFs = new StatFs(sdPath.getPath());
			long blockSize = sFs.getBlockSize();
			long availableBlocks = sFs.getAvailableBlocks();
			long sdCardSize = (blockSize * availableBlocks)/(1024*1024); //M
			if (sdCardSize < 10) {
				result = 1;
			}
		}else {
			result = 2;
		}
		return result;
	}
	
	/**
	 * 获取屏幕分辨率
	 * @param mActivity
	 * @return int[]{width,height}
	 */
	@SuppressWarnings("deprecation")
    public static int[] getResolution(Context ctx) {
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		Display screen = wm.getDefaultDisplay();
		return new int[] { screen.getWidth(), screen.getHeight() };
	}
	
	
	/**
	 * 获取状态栏的高度
	 * (需在屏幕显示后台调用)
	 * @param mActivity
	 * @return
	 */
	public static int getStatusBarHeight(Activity mActivity){
		Rect frame = new Rect();  
		mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		return frame.top; 
	}
	
	
	/**
	 * 获取标题栏高度
	 * (需在屏幕显示后台调用)
	 * @param mActivity
	 * @return
	 */
	public static int getTitleBarHeight(Activity mActivity){
		int contentTop = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();  
		return contentTop - getStatusBarHeight(mActivity);
	}
	
	/*
	 * 
	 * 取得手机生产商
	 */
	public static String getBrand()
	{
		String brand = Build.BRAND == null ? "" : Build.BRAND;
		return brand;
	}
	
	/**
	 * 取得手机系统版本
	 * @return
	 */
	public static String getClientOsVersion() {
		String clientOsVersion = "android" + Build.VERSION.RELEASE;
		return clientOsVersion;      
	} 
	
	
	/**
	 * 取得本应用的版本名
	 * @param ctx
	 * @return
	 */
	
	public static String getVersionName(Context ctx) {
		String versionName = "";
		PackageManager manager = ctx.getPackageManager();

		PackageInfo info;
		try {
			info = manager.getPackageInfo(ctx.getPackageName(), 0);
			versionName = info.versionName;
			Log.d(TAG, "versionCode =============" + versionName);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "getVersionName exception:" + e.getMessage());
		}
		return versionName;
	}
	/**
	 * 转换dip为px 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param context
	 * @param dip
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static int convertDIP2PX(Context context, int dip) { 
	    float scale = context.getResources().getDisplayMetrics().density; 
	    return (int)(dip*scale + 0.5f*(dip>=0?1:-1)); 
	} 
	/**
	 * 转换px为dip 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param context
	 * @param px
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static int convertPX2DIP(Context context, int px) { 
	    float scale = context.getResources().getDisplayMetrics().density; 
	    return (int)(px/scale + 0.5f*(px>=0?1:-1)); 
	} 
	
	public static boolean isAdapted800And480(String model){
		boolean flag = false;
		String[] allPhones = {"SCH-i909","S710d","SCH-i919","N880"};
				// "SCH-i919","ZTE-C N880" added by tianh for test
		for(int i=0;i<allPhones.length;i++){
			if(model.indexOf(allPhones[i]) != -1){
				return true;
			}
		}
		
		return flag;
	}
	
	public static boolean isAdapted960And540(String model){
		boolean flag = false;
		String[] allPhones = {"XT882","XT883"};
		for(int i=0;i<allPhones.length;i++){
			if(model.indexOf(allPhones[i]) != -1){
				return true;
			}
		}
		
		return flag;
	}
	
	public static boolean isAdapted1280And720(String model){
		boolean flag = false;
		String[] allPhones = {"XT928", "HTC X720d", "SCH-I939", "SCH-N719"};
		for(int i=0;i<allPhones.length;i++){
			if(model.indexOf(allPhones[i]) != -1){
				flag = true;
				break;
			}
		}
		return flag;
	}
	public static boolean isAdapted1920And1080(String model){
		boolean flag = false;
		String[] allPhones = {"SCH-I959","SM-N9009","SM-N9002","SM-N9006","SM-N9008"};
		for(int i=0;i<allPhones.length;i++){
			if(model.indexOf(allPhones[i]) != -1){
				flag = true;
				break;
			}
		}
		return flag;
	}
}
