/**
 * Copyright  2015
 * All right reserved
  @Name: ViewPageListAct.java 
  @Author: Wudl
  @Date: 2015年8月7日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.common.base;

import java.util.List;

import com.gdtel.eshore.androidframework.ui.adapter.ListViewPagerAdapter;
import com.gdtel.eshore.anroidframework.R;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ListView;

/**
 * Copyright  2015
 * All right reserved
  @Name: ViewPageListAct.java 
  @Author: Wudl
  @Date: 2015年8月7日 
  @Description: 
 */
public class ViewPageListAct extends BaseActivity implements ViewPageInterface<ListView>{

	protected ViewPager viewpager;
	protected List<ListView> listData;
	protected ListViewPagerAdapter pagerAdapter;
	protected OnPageChangeListener pageListener;

	
	private TaskCallBack<Integer> callBack;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpage_layout);
	}

	/* (non-Javadoc)
	 * @see com.gdtel.eshore.androidframework.common.base.ViewPageInterface#initViewPage(java.util.List)
	 */
	@Override
	public void initViewPage(List<ListView> lisData) {

		viewpager = (ViewPager)findViewById(R.id.viewpager);
		
		pagerAdapter = new ListViewPagerAdapter(lisData,ViewPageListAct.this);
		
		viewpager.setAdapter(pagerAdapter);
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
		viewpager.addOnPageChangeListener(pageListener);
	}

	/* (non-Javadoc)
	 * @see com.gdtel.eshore.androidframework.common.base.ViewPageInterface#initPageLayout()
	 */
	@Override
	public List<ListView> initPageLayout() {

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
