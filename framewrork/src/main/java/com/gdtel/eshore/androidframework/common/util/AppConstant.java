package com.gdtel.eshore.androidframework.common.util;

import android.app.Application;

public class AppConstant {
	public static Application application;
	public static final String PREFS_FILE = "assistant.prefs";
	public static boolean LOG_FILE = false;// 文件日志
	public static boolean LOGGINGENABLED = true;// 日志
	public static boolean CONNECT_OPEN = false;// 是否有网络
	public static boolean ISNEWVERSION;// 是否新版本
	public static boolean ENFORCE;// 是否强制更新
	public static final String NEWDOWNLOADADDRESS = "";// 下载地址
	public static final String NEWVERSION_CONTENT = "";// 新版本描述
	public static final String UNIQUEID = ""; // 手机唯一标识
	public static final String VERSIONTEXT = "";// 版本说明
	public static final byte UPLOADED = 0;// 已上传
	public static final byte NOUPLOADED = 1;// 未上传
	public static final byte DELETED = 2;// 未上传
	public static int SCREENWIDTH;// 屏幕宽
	public static int SCREENHEIGHT;// 屏幕高
	public static final String DATA_ERROR = "无法连接网络";
	public static final String PARSE_ERROR = "请重试";// "解析错误请重新尝试请求"

	public static boolean ISPROXY = false; // 是否使用代理
	public static final String CONNECT_EMPTY = DATA_ERROR;
	public static final String NETWORD_TIMEOUT = "网络连接超时";
	public static String LONGITUDE;// 经度
	public static String LATITUDE;// 维度
	public static final String RESULT_OK = "1";// 0为失败，1为成功
	public static final String APP_ITEM_TITLE = "title";
	public static String BASE_URL = "";
	
	/**
	 * 加密key
	 */
	public static final String SECRET_KEY []=
		{//cbc模式的key
			"Lak4jds5",
			"7i)n3?q^",
			"9E@1p7W|",
			"8;d)P7!B",
			"9_v@<D4q",
			"9-l&d#]8",
			"7*-l#]8S"
		};
	public static boolean ISREAL = false; // 是否正式环境
    public static boolean ISNETWORK = false;
	
	static{
	    if(ISREAL){//正式环境
	        BASE_URL = "http://192.168.199.246:132/proxyservice/softVersionServlet.htm";
	    }else{//测试环境
	        BASE_URL = "http://192.168.1.187:11604";
	    }
	}
	
}