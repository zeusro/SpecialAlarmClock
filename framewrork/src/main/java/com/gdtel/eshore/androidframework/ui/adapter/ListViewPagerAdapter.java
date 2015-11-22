/**
 * Copyright  2015
 * All right reserved
  @Name: ListViewPagerAdapter.java 
  @Author: Wudl
  @Date: 2015年8月7日 
  @Description: 
 */
package com.gdtel.eshore.androidframework.ui.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Copyright  2015
 * All right reserved
  @Name: ListViewPagerAdapter.java 
  @Author: Wudl
  @Date: 2015年8月7日 
  @Description: 
 */
public class ListViewPagerAdapter extends PagerAdapter {

	protected List<ListView> viewList;
	private Context mContext;

	public ListViewPagerAdapter(Context context) {
		super();
		this.mContext = context;
	}

	public ListViewPagerAdapter(List<ListView> viewList, Context context) {
		super();
		this.viewList = viewList;
		this.mContext = context;
	}

	@Override
	public int getCount() {

		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		container.removeView(viewList.get(position));
	}

	@Override
	public int getItemPosition(Object object) {

		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		container.addView(viewList.get(position));
		return viewList.get(position);
	}

}
