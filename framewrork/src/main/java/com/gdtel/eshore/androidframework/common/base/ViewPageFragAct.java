/**
 * Copyright  2015
 * All right reserved
  @Name: ViewPageAct.java 
  @Author: Wudl
  @Date: 2015年8月5日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.common.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.gdtel.eshore.anroidframework.R;

import java.util.List;

/**
 * Copyright  2015
 * All right reserved
  @Name: ViewPageAct.java 
  @Author: Wudl
  @Date: 2015年8月5日 
  @Description: 
 */
public class ViewPageFragAct extends FragmentActivity implements ViewPageInterface<Fragment>{
	
	protected ViewPager viewPager;
	protected List<Fragment> listData;
	protected FragmentPagerAdapter fpAdapter;
	protected OnPageChangeListener pageListener;
	
	private TaskCallBack<Integer> callBack;

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		setContentView(R.layout.viewpage_layout);
	}

	/**
	 * 
	 * Copyright  2015
	 * All right reserved
	  @Type:void
	  @Author: Wudl 
	  @Date: 2015年8月5日 
	  @Description:
	 */
	@Override
	public void initViewPage(List<Fragment> lisData){
		viewPager = (ViewPager)findViewById(R.id.viewpager);
		
		fpAdapter =new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return listData.size();
			}
			@Override
			public Fragment getItem(int arg0)
			{
				return listData.get(arg0);
			}
		};
		//设置适配器
		viewPager.setAdapter(fpAdapter);
		pageListener = new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {

				if (callBack != null) {
					callBack.callBackResult(arg0);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {

				
			}
		};
		viewPager.addOnPageChangeListener(pageListener);
	}

	/* (non-Javadoc)
	 * @see com.gdtel.eshore.androidframework.common.base.ViewPageInterface#initPageLayout()
	 */
	@Override
	public List<Fragment> initPageLayout() {

		return null;
	}

	/* (non-Javadoc)
	 * @see com.gdtel.eshore.androidframework.common.base.ViewPageInterface#setTaskCallBack(com.gdtel.eshore.androidframework.common.base.TaskCallBack)
	 */
	@Override
	public void setTaskCallBack(TaskCallBack<Integer> callBack) {

		this.callBack = callBack;
	}

	
	

}
