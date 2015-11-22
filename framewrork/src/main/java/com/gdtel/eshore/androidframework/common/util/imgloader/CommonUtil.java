package com.gdtel.eshore.androidframework.common.util.imgloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {

	/**
	 * 根据包名判断应用程序是否已安装
	 * 
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static boolean isPackageNameExist(Context context, String packagename) {
		PackageInfo packageInfo = null;
		if(TextUtils.isEmpty(packagename)) return false;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packagename, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 显示是否下载应用
	 * 
	 * @param activity
	 * @param title
	 *            标题
	 * @param message
	 *            提示信息
	 */
	public static void ShowDownloadDialog(final Context ctx,final String appName,final String appAndroidUrl) {
		if (!((Activity) ctx).isFinishing()) {
			new AlertDialog.Builder(ctx)
					.setMessage("未检测到该软件，您确定立即下载" + appName + "吗？")
					.setTitle("提示")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Uri uri = Uri.parse(appAndroidUrl);
									Intent downloadIntent = new Intent(
											Intent.ACTION_VIEW, uri);
									ctx.startActivity(downloadIntent);
									dialog.cancel();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).show();
		}
	}

private static final CommonLog log = LogFactory.createLog();
    
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        } 
        return true;
    }
    
    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
        }
    }
    
    
    public static boolean checkNetState(Context context){
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) 
                    {
                        netstate = true;
                        break;
                    }
                }
            }
        }
        return netstate;
    }
    
    public static void showToask(Context context, String tip){
        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
    
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

}
