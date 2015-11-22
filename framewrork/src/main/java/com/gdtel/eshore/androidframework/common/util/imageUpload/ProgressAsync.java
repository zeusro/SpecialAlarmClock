package com.gdtel.eshore.androidframework.common.util.imageUpload;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
import com.gdtel.eshore.androidframework.common.util.security.DesUtils;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ProgressAsync extends AsyncTask<String, Integer, String> {
    private long totalSize;
    private Context context;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private UploadListener uploadListener;
    private int code;
    private int key;
    private Handler handler;
    private boolean flag = true;


    public ProgressAsync(Context context, Bitmap bitmap,
                         int code, UploadListener uploadListener, int key, boolean flag) {
        init();
        this.context = context;
        this.bitmap = bitmap;
        this.bitmap.toString();
        this.uploadListener = uploadListener;
        this.code = code;
        this.key = key;
        this.flag = flag;
    }

    public ProgressAsync(Context context, Bitmap bitmap,
                         int code, UploadListener uploadListener, int key) {
        init();
        this.context = context;
        this.bitmap = bitmap;
        this.uploadListener = uploadListener;
        this.code = code;
        this.key = key;
    }

    private void init() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        long progressSize = (Long) msg.obj;
                        uploadListener.Uploading(progressSize,
                                (int) (100 * progressSize / totalSize), totalSize);
                        break;
                }
            }
        };
    }

    public static enum ContentType {
        JSON, XML, APPLICATION_OCTET_STREAM
    }

    ;

    @Override
    protected void onPreExecute() {//执行前的初始化

        if (flag) {
            /** 初始化进度条对话框  */
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("请稍等...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {//执行任务
        /**服务器路径**/
        String url = params[0];
        String paramsJson = params[1];

        //获取位图
        InputStreamEntity reqEntity;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //将位图写进输入流
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
        if (!TextUtils.isEmpty(paramsJson)) {
            DebugLog.i("即将加密的明文未转换", paramsJson);
            // 加密后的字节数组
            byte[] bytes = DesUtils.encrypt(paramsJson, key);
            String param = DesUtils.toHexString(bytes);
            DebugLog.i("加密后的字符串", param);
            // Base64编码
            String base64Json = android.util.Base64.encodeToString(bytes,
                    android.util.Base64.DEFAULT);
            DebugLog.i("加密后加bytes编码即将上传的字符串", base64Json);

            builder.addTextBody("Token", base64Json);//设置请求参数
        }

        reqEntity = new InputStreamEntity(sbs, -1);
        org.apache.http.entity.ContentType contentType = org.apache.http.entity.ContentType.getOrDefault((HttpEntity) reqEntity);
        builder.addBinaryBody("image", sbs,
                contentType, "sb.png");

        totalSize = baos.size();//获取上传文件的大小
        uploadListener.UploadPre(totalSize);


        HttpEntity entity = builder.build();// 生成 HTTP POST 实体

        long size = entity.getContentLength();

        ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(entity, new ProgressOutHttpEntity.ProgressListener() {
            @Override
            public void transferred(long transferedBytes) {
                int progress = (int) (100 * transferedBytes / totalSize);
                if (progress >= 100) {
                    progress = 99;
                }
                publishProgress(progress);//更新进度
                Message msg = Message.obtain();
                msg.obj = transferedBytes;
                msg.what = 0;
                handler.sendMessage(msg);
            }
        });
        return MethodUploadImage.uploadFile(url, progressHttpEntity);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {//执行进度

        Log.i("info", "values:" + values[0]);
        if (flag) {
            progressDialog.setProgress((int) values[0]);//更新进度条
        }

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {//执行结果

        Log.i("info", result);
        //		Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if (flag) {
            progressDialog.setProgress(100);//更新进度条
            progressDialog.dismiss();
        }
        uploadListener.Uploaded(code, result);
        super.onPostExecute(result);
    }
}
