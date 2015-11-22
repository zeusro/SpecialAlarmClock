package com.gdtel.eshore.androidframework.ui.view.report.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class TableView4ScrollView_lib extends HorizontalScrollView {
	ScrollViewObserver_Test mScrollViewObserver = new ScrollViewObserver_Test();
	private TableView4ScrollView_lib unmove_scroll_view = null;
	
	public TableView4ScrollView_lib(Context context) {
		super(context);
	}

	public TableView4ScrollView_lib(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (mScrollViewObserver != null /*&& (l != oldl || t != oldt)*/) {
			mScrollViewObserver.NotifyOnScrollChanged(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	public void AddOnScrollChangedListener(OnScrollChangedListener_Test listener) {
		mScrollViewObserver.AddOnScrollChangedListener(listener);
	}
	
	public void RemoveOnScrollChangedListener(OnScrollChangedListener_Test listener) {
		mScrollViewObserver.RemoveOnScrollChangedListener(listener);
	}
	
	public void setUnmoveScrollview(TableView4ScrollView_lib scrollview){
		this.unmove_scroll_view = scrollview;
	}

	public static interface OnScrollChangedListener_Test {
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}
	
	public class ScrollViewObserver_Test {
		List<OnScrollChangedListener_Test> mList;

		public ScrollViewObserver_Test() {
			super();
			mList = new ArrayList<OnScrollChangedListener_Test>();
		}

		public void AddOnScrollChangedListener(OnScrollChangedListener_Test listener) {
			mList.add(listener);
		}

		public void RemoveOnScrollChangedListener(
				OnScrollChangedListener_Test listener) {
			mList.remove(listener);
		}

		public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
			if (mList == null || mList.size() == 0) {
				return;
			}
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i) != null && !mList.get(i).equals(unmove_scroll_view)) {
					mList.get(i).onScrollChanged(l, t, oldl, oldt);
				}
			}
		}
	}
}
