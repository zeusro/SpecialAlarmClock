package com.gdtel.eshore.androidframework.component.upgrade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.gdtel.eshore.androidframework.common.net.http.MethodParameters;
import com.gdtel.eshore.androidframework.common.net.http.NetworkAccess;
import com.gdtel.eshore.androidframework.common.util.AppConstant;
import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
import com.gdtel.eshore.anroidframework.R;

import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * 版本升级工具
 * <功能详细描述>
 *
 * @author youjw
 * @version [版本号, 2013-12-23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UpgradeUtils {

    private static final String tag = UpgradeUtils.class.getSimpleName();
    private static ProgressDialog progressDialog;

    /**
     * 检测新版本
     *
     * @param ctx
     */
    public static void checkNewVersion(final Activity ctx) {
        checkNewVersion(ctx, false);
    }

    /**
     * 检测新版本
     *
     * @param ctx
     * @param isShowNoUpdateTip 是否显示更新提示
     */
    public static void checkNewVersion(final Activity ctx, final boolean isShowNoUpdateTip) {

        new Thread() {
            @Override
            public void run() {
                try {
                    if (isShowNoUpdateTip) {
                        ctx.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog = ProgressDialog.show(ctx, null, "正在检查更新，请稍候...",
                                        true, true);
                            }
                        });
                    }
                    PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                            0);
                    String mVersonName = info.versionName;
                    DebugLog.d(tag, "versionName:" + mVersonName);
                    final VersionInfo mInfo = updateSoftVersionDetail(ctx);

                    if (isShowNoUpdateTip) {
                        ctx.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!ctx.isFinishing() && progressDialog != null
                                        && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }

                    // 当前版本小于服务器版本时才提示用户更新
                    if (mInfo != null && mVersonName != null
                            && mInfo.latestVersion != null && mVersonName.compareTo(mInfo.latestVersion) < 0) {
                        mInfo.localVersion = mVersonName;
                        ctx.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showUpgradeDialog(ctx, mInfo);
                            }
                        });
                        saveNewVersionInfo(mInfo, ctx);
                    } else {
                        if (isShowNoUpdateTip) {
                            ctx.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showNoUpdateTip(ctx);
                                }
                            });
                        }
                    }
                } catch (NullPointerException e) {
                    checkNewVersion(ctx, isShowNoUpdateTip);
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    /**
     * 将版本信息持久化
     *
     * @param info
     */
    private static void saveNewVersionInfo(VersionInfo info, Activity ctx) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(ctx.openFileOutput(
                    "VersionInfo", Context.MODE_PRIVATE));
            out.writeObject(info);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送新版本更新通知
     *
     * @param versionInfo
     */
    @SuppressWarnings("deprecation")
    private static void notifycation(final Activity ctx, final VersionInfo versionInfo) {
        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(ctx, DownloadView.class);
        notificationIntent.putExtra("versionInfo", versionInfo);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        String titleText = "发现有新版本"; // 通知标题文本()
        String contentText = "点击可下载最新版本升级";
//todo 待测试:
//        Notification notification = new Notification(R.mipmap.ic_launcher, titleText, System.currentTimeMillis());
        Notification notification = new Notification.Builder(ctx)
                .setContentTitle(titleText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
//以下为后期添加
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true).getNotification();

//        notification.setLatestEventInfo(ctx, titleText, contentText, contentIntent);
        mNotificationManager.notify(R.mipmap.ic_launcher, notification);
    }

    /**
     * 显示升级对话框
     *
     * @param activity
     * @param versionInfo
     */
    private static void showUpgradeDialog(final Activity activity, final VersionInfo versionInfo) {
        String msg = null;
        int iconId = 0;
        //isoptional 0：强制更新 1：可选择更新
        if ("1".equals(versionInfo.updateForce)) {
            msg = "发现新版本:V" + versionInfo.latestVersion + "，确定更新？\n";
            iconId = android.R.drawable.ic_menu_help;
        } else {
            msg = "发现新版本:V" + versionInfo.latestVersion + "，只有更新到最新版本才能使用!\n";
            iconId = android.R.drawable.ic_dialog_alert;
        }
//		String des = "";
//		try
//		{
//			des = new String((msg + versionInfo.desc).getBytes(), "UTF-8");
//		} catch (UnsupportedEncodingException e)
//		{
//			e.printStackTrace();
//		}
        Builder builder = new AlertDialog.Builder(activity).setIcon(iconId).setTitle("升级提醒")
                .setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(activity, DownloadView.class);
                        it.putExtra("versionInfo", versionInfo);
                        activity.startActivity(it);
                    }
                });
        if ("1".equals(versionInfo.updateForce)) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    notifycation(activity, versionInfo);
                }
            });
        }
        builder.setCancelable(false);
        builder.show();
    }

    private static void showNoUpdateTip(final Activity activity) {
        new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("升级提醒").setMessage("您的版本已是最新版本！").setPositiveButton("确定", null).show();

    }

    /**
     * 下载自升级文件
     *
     * @param context
     * @param url
     * @param file
     * @param callback
     * @return
     */
    @SuppressLint("WorldReadableFiles")
    public static boolean downloadFile(final Context context, String url,
                                       final File file, final DownloadCallback callback) {
        boolean isSccuess = false;
        try {
            Log.d("Result", "---url:" + url);
            RestTemplate restTemplate = new RestTemplate(true);
            isSccuess = restTemplate.execute(url, HttpMethod.GET, null,
                    new ResponseExtractor<Boolean>() {

                        @SuppressWarnings("deprecation")
                        @Override
                        public Boolean extractData(ClientHttpResponse response)
                                throws IOException {
                            DataInputStream in = null;
                            DataOutputStream out = null;
                            int downloadSize = 0;
                            boolean isCancelled = false;
                            boolean isSccuess = false;

                            try {
                                HttpHeaders headers = response.getHeaders();
                                int total = 0;
                                if (headers != null) {
                                    total = (int) headers.getContentLength();
                                }
                                if (response.getStatusCode() == HttpStatus.OK) {
                                    in = new DataInputStream(response.getBody());
                                }

                                out = new DataOutputStream(context
                                        .openFileOutput(file.getName(),
                                                Context.MODE_WORLD_READABLE));// 使用MODE_WORLD_READABLE才能正常安装
                                byte[] buffer = new byte[1024];
                                int count = 0;
                                int loopCount = 0;// 循环次数
                                while (!isCancelled
                                        && (count = in.read(buffer)) > 0) {
                                    out.write(buffer, 0, count);
                                    downloadSize += count;
                                    int progress = (int) (downloadSize * 100 / total);
                                    if (++loopCount % 20 == 0 || progress == 100) {
                                        if (callback != null) {
                                            isCancelled = callback
                                                    .onProgressChanged(progress,
                                                            downloadSize, total);
                                        }
                                    }
                                }
                                if (downloadSize == total) {// 下载成功
                                    if (callback != null) {
                                        callback.onProgressChanged(100,
                                                downloadSize, total);
                                    }
                                    isSccuess = true;
                                }
                                if (isCancelled) {
                                    file.delete();
                                }
                            } catch (Exception e) {
                                Log.d(tag, "" + e.getMessage());
                            }

                            return isSccuess;
                        }
                    });

        } catch (Exception e) {
            Log.d(tag, "" + e.getMessage());
        }

        return isSccuess;
    }

    /**
     * 查询服务器版本信息
     *
     * @param context
     * @return
     * @throws JSONException
     */
    public static VersionInfo updateSoftVersionDetail(Context context) throws Exception {
        //String urlString = "http://locahost:8080/CHMOService/softVersionServlet.htm";
//		String urlString = "http://192.168.199.246:132/proxyservice/softVersionServlet.htm";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cmd", "UpdateSoftVersion");//UpdateSoftVersion  ("method", "updateSoftVersion")
        params.put("imsi", getImsi(context));
        params.put("esm", getEsn((Activity) context));
        params.put("category", "android");
        params.put("tercode", Build.MODEL);
        params.put("version", "");
        params.put("v", "");
        params.put("classid", "0x99997777");
        VersionInfo versionInfo = new VersionInfo();
        MethodParameters.testUrl(params, AppConstant.BASE_URL);

	    /*
         * 20150808 许绍林
		 * 为了测试暂时屏蔽下面的方法
		 */
        versionInfo = new NetworkAccess().getEntityByStream(AppConstant.BASE_URL, params, VersionInfo.class, NetworkAccess.ContentType.JSON, NetworkAccess.MethodType.POST);

//		versionInfo.localVersion = "1.0";
//		versionInfo.latestVersion = "1.1";
//		versionInfo.updateForce = "1";
//		versionInfo.updateURL = "162.163";
//		versionInfo.updateDesc = "";
//		versionInfo.softlength = "161654";
//		versionInfo.appCode = "0";
//		versionInfo.result = "true";

        return versionInfo;
    }

    private static String getImsi(final Context mActivity) {
        TelephonyManager telephonyManager = (TelephonyManager) mActivity
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getSubscriberId();
        return imei;
    }

    private static String getEsn(final Activity mActivity) {
        TelephonyManager tm = (TelephonyManager) mActivity
                .getSystemService(Context.TELEPHONY_SERVICE);
        String esn = tm.getDeviceId();
        return esn;
    }


    /**
     * 许绍林
     * 检查版本，
     * 如果有新版本，弹出通知。
     *
     * @param ctx
     */
    @SuppressWarnings("deprecation")
    public static void openNotifycation(final Activity ctx) {
        //检查更新
        try {
            final VersionInfo versionInfo = UpgradeUtils.updateSoftVersionDetail(ctx);
            String mVersonName = getVersion(ctx);
            // 当前版本小于服务器版本时才提示用户更新
            if (versionInfo != null && mVersonName != null
                    && versionInfo.latestVersion != null && mVersonName.compareTo(versionInfo.latestVersion) < 0) {
                versionInfo.localVersion = mVersonName;
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifycation(ctx, versionInfo);
                    }
                });
                saveNewVersionInfo(versionInfo, ctx);
            } else {


            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static String getVersion(Activity ctx) {
        PackageInfo info;
        String mVersonName = null;
        try {
            info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    0);
            mVersonName = info.versionName;
        } catch (NameNotFoundException e) {

            e.printStackTrace();
        }

        return mVersonName;
    }
}
