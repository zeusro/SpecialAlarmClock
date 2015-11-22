/*
 * 文 件 名:  SelectPhoto.java
 * 版    权:  Eshore Techonlogy Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  youjw
 * 修改时间:  2014-11-25
 * 修改内容:  <修改内容>
 */
package com.gdtel.eshore.androidframework.common.util.photo;

import java.io.File;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 图片选择类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SelectPhoto {
    
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 相册
    public static final int PHOTORESOULT = 3;// 结果

    public static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String TAG = "SelectPhoto";
    /**
     * 选择图片
     * <功能详细描述>
     * @param context
     * @param code 1,拍照 2,相册
     * @param path 保存路径
     * @return true 成功，false 失败
     * @see [类、类#方法、类#成员]
     */
    public boolean selectPhoto(Activity context,int code,String path){
        boolean isRes = false;
        if (code == PHOTOZOOM) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
            isRes = true;
            context.startActivityForResult(intent, code);
        } else if (code == PHOTOHRAPH){
            if(TextUtils.isEmpty(path)) {
                DebugLog.d(TAG, "保存图片路径为空，path="+path);
                return isRes;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Environment.getExternalStorageDirectory(), "ketwangwai_temp.jpg")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
            isRes = true;
            context.startActivityForResult(intent, code);
        }
        return isRes;
    }
    /**
     * 裁剪图片
     * <功能详细描述>
     * @param context
     * @param uri
     * @param aspectX  是宽高的比例  默认传 1
     * @param aspectY  默认传 1
     * @param outputX  裁剪图片宽高 默认传 64
     * @param outputY  默认传 64
     * @see [类、类#方法、类#成员]
     */
    public void startPhotoZoom(Activity context,Uri uri,int aspectX,int aspectY,int outputX,int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, PHOTORESOULT);
}
}
