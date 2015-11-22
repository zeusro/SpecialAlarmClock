//package com.gdtel.eshore.androidframework.common.base;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//import android.content.res.Configuration;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//
//import com.gdtel.eshore.androidframework.common.net.http.NetworkAccess;
//import com.gdtel.eshore.androidframework.common.util.AppConstant;
//import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
//
//import org.springframework.web.client.RestClientException;
//
//import java.util.LinkedList;
//import java.util.List;
//
//
//public class MainApp extends Application {
//    private static final String TAG = "MainApp";
//    private static MainApp instance;
//    private Context ctx;
//    private List<Activity> mList = new LinkedList<Activity>();
//    //	private boolean isAddAttention = false;
//    public static final String TESTURL = "http://wap.baidu.com";
//
//    public Context getCtx() {
//        return ctx;
//    }
//
//    public void setCtx(Context ctx) {
//        this.ctx = ctx;
//    }
//
//    public static MainApp getInstance() {
//        return instance;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
////		Intent serviceIntent = new Intent(AppConstant.ACTION_MESSAGE_SERVICE);
////        startService(serviceIntent);
//        instance = this;
//        ctx = getBaseContext();
//        getScreenSize();// 获取屏幕宽高
//    }
//
//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//    }
//
//    /**
//     * �?��网络状�?
//     *
//     * @return
//     */
//
//    public boolean checkNetState() {
//
//        try {
//            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo info = conn.getActiveNetworkInfo();
//            if (info != null) { // info != null 网络连接已开�?
//                String typeName = info.getTypeName(); // wifi/mobile
//                typeName = typeName.toLowerCase();
//                if (typeName.indexOf("wifi") != -1) {
//                    AppConstant.ISPROXY = false;
//                } else if (typeName.indexOf("mobile") != -1) {
//                    String extraInfo = info.getExtraInfo();
//                    extraInfo = extraInfo.toLowerCase();
//                    if (extraInfo.indexOf("net") != -1) {
//                        AppConstant.ISPROXY = false;
//                    } else if (extraInfo.indexOf("wap") != -1) {
////						Data.isProxy = true;
//                        AppConstant.ISPROXY = false;
//                    }
//                }
//                AppConstant.CONNECT_OPEN = true;
//            } else { // info == null 网络连接关闭
//                AppConstant.CONNECT_OPEN = false;
//            }
//        } catch (Exception e) {
//            DebugLog.e(TAG, "" + e.getMessage());
//            e.printStackTrace();
//        }
//        return AppConstant.CONNECT_OPEN;
//    }
//
//    /**
//     * 获取屏幕尺寸
//     */
//    public void getScreenSize() {
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = getApplicationContext().getResources().getDisplayMetrics();
//        AppConstant.SCREENWIDTH = dm.widthPixels;
//        AppConstant.SCREENHEIGHT = dm.heightPixels;
//    }
//
//    public void addActivity(Activity activity) {
//        mList.add(activity);
//    }
//
//    public void exit(boolean flag) {
//        try {
//            for (Activity activity : mList) {
//                if (flag) {
//                    if (activity != null) {
//                        activity.finish();
//                    }
//                } else {
////					if (activity != null&&!(activity instanceof ViewDataMainActivity)){
////						activity.finish();
////					}
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
////            System.exit(0);
//        }
//    }
//
//
//    /**
//     * 检查是否能够上网
//     * <功能详细描述>
//     *
//     * @param url
//     * @return
//     * @see [类、类#方法、类#成员]
//     */
//    public boolean checkNet(String url) {
//        AppConstant.ISNETWORK = false;
//        DebugLog.i(TAG, "checkNet:--> url=" + url + " checkNetState()=" + checkNetState());
//        if (!checkNetState()) return AppConstant.ISNETWORK;
//        if (TextUtils.isEmpty(url)) return AppConstant.ISNETWORK;
//        try {
//            byte[] b = new NetworkAccess().getStream(url);
//            if (b != null)
//                AppConstant.ISNETWORK = true;
//        } catch (RestClientException ex) {
//
//            DebugLog.i(TAG, "checkNet:--> " + ex.getMessage());
////            ex.printStackTrace();
//        }
//        DebugLog.i(TAG, "checkNet:--> AppConstant.ISNETWORK=" + AppConstant.ISNETWORK);
//        return AppConstant.ISNETWORK;
//    }
///*	public boolean isAddAttention() {
//        return isAddAttention;
//	}
//
//	public void setAddAttention(boolean isAddAttention) {
//		this.isAddAttention = isAddAttention;
//	}*/
//}
