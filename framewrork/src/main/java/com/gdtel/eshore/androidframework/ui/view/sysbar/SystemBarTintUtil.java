/**
 * Copyright  2015
 * All right reserved
  @Name: SystemBarTintUtil.java 
  @Author: Wudl
  @Date: 2015年11月13日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.ui.view.sysbar;

import com.gdtel.eshore.anroidframework.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;


/**
 * Copyright  2015
 * All right reserved
  @Name: SystemBarTintUtil.java 
  @Author: Wudl
  @Date: 2015年11月13日 
  @Description: 
 */
public class SystemBarTintUtil {
	
	
	public void initSystemTint(Activity mActivity, int resource){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(mActivity,true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(resource);
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void setTranslucentStatus(Activity mActivity,boolean on) {
		Window win = mActivity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

}
