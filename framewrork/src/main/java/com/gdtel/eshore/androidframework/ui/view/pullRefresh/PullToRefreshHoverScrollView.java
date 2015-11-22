package com.gdtel.eshore.androidframework.ui.view.pullRefresh;

import com.gdtel.eshore.androidframework.ui.view.scroller.HoverScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 封装了自定义HoverScrollView的下拉刷新
 * 
 * @author Linjiaodu
 * @since 2015-9-29
 */
public class PullToRefreshHoverScrollView extends PullToRefreshBase<HoverScrollView>{
	
	    /**
	     * 构造方法
	     * 
	     * @param context context
	     */
	    public PullToRefreshHoverScrollView(Context context) {
	        this(context, null);
	    }
	    
	    /**
	     * 构造方法
	     * 
	     * @param context context
	     * @param attrs attrs
	     */
	    public PullToRefreshHoverScrollView(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }
	    
	    /**
	     * 构造方法
	     * 
	     * @param context context
	     * @param attrs attrs
	     * @param defStyle defStyle
	     */
	    public PullToRefreshHoverScrollView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }

	    /**
	     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#createRefreshableView(android.content.Context, android.util.AttributeSet)
	     */
	    @Override
	    protected HoverScrollView createRefreshableView(Context context, AttributeSet attrs) {
	    	HoverScrollView scrollView = new HoverScrollView(context);
	        return scrollView;
	    }

	    /**
	     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullDown()
	     */
	    @Override
	    protected boolean isReadyForPullDown() {
	        return mRefreshableView.getScrollY() == 0;
	    }

	    /**
	     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullUp()
	     */
	    @Override
	    protected boolean isReadyForPullUp() {
	        View scrollViewChild = mRefreshableView.getChildAt(0);
	        if (null != scrollViewChild) {
	            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
	        }
	        
	        return false;
	    }

}
