package com.gdtel.eshore.androidframework.component.upgrade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
import com.gdtel.eshore.anroidframework.R;

import java.io.File;
import java.io.ObjectInputStream;

/**
 * 下载视图
 * 功能：
 * 1.显示下载信息：如本地版本，服务器版本
 * 2.下载控制：是否下载
 *
 * @author youjw
 * @version [版本号, 2013-12-23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@SuppressLint("HandlerLeak")
public class DownloadView extends Activity {

    private static String tag = "DownloadView";
    private VersionInfo mInfo;
    private ProgressBar progressBar;//进度条
    private TextView progress_text, server_des;
    private DownloadTask downloadTask;//下载线程
    private Button btn_cancel;
    private File file;
    private NotificationManager manager;
    private RemoteViews view;
    private PendingIntent pIntent;
    private Notification notification = new Notification();
    private Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            //todo 待测试:
            if (downloadTask != null && !downloadTask.isCancelled) {
                if (msg.arg1 < 0) {
                    notification = new Notification.Builder(DownloadView.this)
                            .setContentTitle(getString(R.string.app_name))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText("下载失败,点击可重新下载")
                            .setContentIntent(pIntent)
//以下为后期添加
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setOngoing(true).getNotification();

//                    notification.setLatestEventInfo(DownloadView.this, getString(R.string.app_name), "下载失败,点击可重新下载", pIntent);
                    manager.notify(R.mipmap.ic_launcher, notification);
                } else if (msg.arg1 < 100) {
                    view.setProgressBar(R.id.pb, 100, msg.arg1, false);
                    view.setTextViewText(R.id.tv, "下载" + msg.arg1 + "%");
                    manager.notify(R.mipmap.ic_launcher, notification);
                } else if (msg.arg1 == 100) {
                    view.setTextViewText(R.id.tv, "下载完成");
                    manager.cancel(R.mipmap.ic_launcher);//清除通知
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_progress_update);
        mInfo = (VersionInfo) getIntent().getSerializableExtra("versionInfo");
        if (mInfo == null) {
            getNewVersionInfo();
        }
        if (mInfo == null) return;
        initApkFile();
        setupView();

        downloadTask = new DownloadTask();
        downloadTask.execute(mInfo.DownloadLink);
    }


    /**
     * 从 本地取新版本信息
     */
    private void getNewVersionInfo() {
        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("VersionInfo"));
            mInfo = (VersionInfo) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initApkFile() {
        try {
            String apkName = getPackageName() + ".apk";
            file = new File(getFilesDir(), apkName);
            if (file.exists()) {
                file.delete();
                Log.i(tag, "file is deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupView() {
        progressBar = (ProgressBar) findViewById(R.id.download_progress);
//		btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
//		btn_confirm.setOnClickListener(buttonclick);
        btn_cancel.setOnClickListener(buttonclick);

        if ("true".equals(mInfo.IsUpdateForcibly)) {
            btn_cancel.setVisibility(View.GONE);
        }

        server_des = (TextView) findViewById(R.id.server_des);
        progress_text = (TextView) findViewById(R.id.progress_text);
        TextView updateDesc = (TextView) findViewById(R.id.updateDesc);
        StringBuilder desc = new StringBuilder("当前版本：").append(mInfo.localVersion).append("\n");
        desc.append("服务器版本：").append(mInfo.Version);
        updateDesc.setText(desc);
        server_des.setText(mInfo.ReleaseNote);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        view = new RemoteViews(getPackageName(), R.layout.upgrade_progress_notice);
        Intent intent = new Intent(this, DownloadView.class);
        pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.contentView = view;
        notification.contentIntent = pIntent;
        // 通知的图标必须设置(其他属性为可选设置),否则通知无法显示
        notification.icon = R.mipmap.ic_launcher;
        view.setImageViewResource(R.id.image, R.mipmap.ic_launcher);// 起一个线程用来更新progress
    }

    private View.OnClickListener buttonclick = new OnClickListener() {
        @Override
        public void onClick(View v) {

            int id = v.getId();
            if (id == R.id.btn_cancel) {
                finish();
                if (downloadTask != null) {
                    downloadTask.isCancelled = true;
                }
                manager.cancel(R.mipmap.ic_launcher);
            }
        }
    };

    /**
     * 下载进程
     */
    private class DownloadTask extends AsyncTask<String, Integer, Boolean> {

        private boolean isCancelled;//是否已取消

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
//			btn_confirm.setEnabled(false);
            progress_text.setText("开始下载...");
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean isSccuess = UpgradeUtils.downloadFile(DownloadView.this, params[0] != null ? params[0].replaceAll(" ", "") : "", file, new DownloadCallback() {
                @Override
                public boolean onProgressChanged(int progress, int downloadSize, int total) {
                    publishProgress(progress, downloadSize, total);
                    sendMsg(progress);
                    return isCancelled;
                }
            });

            //下载失败
            if (!isSccuess) {
                sendMsg(-1);
            }
            return isSccuess;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            progress_text.setText("已下载" + values[1] / 1024 + "K/" + values[2] / 1024 + "K   " + values[0] + "%");
        }

        @Override
        public void onPostExecute(Boolean isSccuess) {//安装apk
            if (isSccuess) {
                DebugLog.d(tag, "file_path:" + file.getAbsolutePath());
                Intent apkintent = new Intent(Intent.ACTION_VIEW);
                apkintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri puri = Uri.fromFile(file);
                apkintent.setDataAndType(puri, "application/vnd.android.package-archive");
                startActivity(apkintent);
            }
            finish();
        }
    }

    /**
     * 发送进度消息
     *
     * @param progress
     */
    private void sendMsg(int progress) {
        Message msg = Message.obtain();
        msg.arg1 = progress;
        handler.sendMessage(msg);
    }

    @Override
    public void onBackPressed() {
        if ("false".equals(mInfo.IsUpdateForcibly)) {
            moveTaskToBack(true);
        }
    }

}