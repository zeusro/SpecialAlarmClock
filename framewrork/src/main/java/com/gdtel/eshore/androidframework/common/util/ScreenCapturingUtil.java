/*
 * 文 件 名:  ScreenCapturingUtil.java
 * 版    权:  Eshore Techonlogy Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  youjw
 * 修改时间:  2014-11-17
 * 修改内容:  <修改内容>
 */
package com.gdtel.eshore.androidframework.common.util;

import java.io.File;
import java.io.FileOutputStream;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 截屏工具类
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2014-11-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ScreenCapturingUtil {

    private static final String TAG = "ScreenCapturingUtil";

    /**
     * 截屏方法
     * <功能详细描述>
     * @param screen_view 截屏界面
     * @param save_file  保存文件名称
     * @return true 成功
     * @see [类、类#方法、类#成员]
     */
    public static boolean screenShot(View screen_view, File save_file) {
        
        if(screen_view == null || save_file == null){
            DebugLog.d(TAG, "screen_view == null"+"or save_file == null");
            return false;
        }
        DebugLog.d(TAG, "屏蔽保存路径  == "+save_file.getAbsolutePath());
        screen_view.setDrawingCacheEnabled(true);
        screen_view.buildDrawingCache();
        
        Bitmap main_curr_bitmap = screen_view.getDrawingCache();
        
        if(main_curr_bitmap != null) {
          //将截图保存
            try{
                if(save_file.exists()){
                    save_file.delete();
                }
                save_file.createNewFile();
                
                FileOutputStream out = new FileOutputStream(save_file);
                main_curr_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            
          //保存成功后将bitmap回收
            if(main_curr_bitmap != null) {
//                main_curr_bitmap.recycle();
                main_curr_bitmap = null;
            }
            DebugLog.d(TAG, "图片截取成功! ");
            return true;
        }
        DebugLog.d(TAG, "图片截取失败 ! ");
        return false;
    }
    /**
     * 截屏方法
     * <功能详细描述>
     * @param screen_view 截屏界面
     * @param filePath  保存路径
     * @return true 成功
     * @see [类、类#方法、类#成员]
     */
    public static boolean screenShot(View screen_view, String filePath) {
        
        if(screen_view == null || filePath == null){
            DebugLog.d(TAG, "screen_view == null"+"or save_file == null");
            return false;
        }
        DebugLog.d(TAG, "屏蔽保存路径  == "+filePath);
        screen_view.setDrawingCacheEnabled(true);
        screen_view.buildDrawingCache();
        
        Bitmap main_curr_bitmap = screen_view.getDrawingCache();
        
        if(main_curr_bitmap != null) {
            //将截图保存
            try{
                File save_file = new File(filePath);
                if(save_file.exists()){
                    save_file.delete();
                }
                save_file.createNewFile();
                
                FileOutputStream out = new FileOutputStream(save_file);
                main_curr_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            
            //保存成功后将bitmap回收
            if(main_curr_bitmap != null) {
//                main_curr_bitmap.recycle();
                main_curr_bitmap = null;
            }
            DebugLog.d(TAG, "图片截取成功! ");
            return true;
        }
        DebugLog.d(TAG, "图片截取失败 ! ");
        return false;
    }
}
