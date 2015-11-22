package com.gdtel.eshore.androidframework.ui.view.pullRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 封装了WebView的下拉刷新
 *
 * @author Li Hong
 * @since 2013-8-22
 */
public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshWebView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#createRefreshableView(android.content.Context, android.util.AttributeSet)
     */
    @Override
    protected WebView createRefreshableView(Context context, AttributeSet attrs) {
        WebView webView = new WebView(context);
        return webView;
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
        // todo 测试
        float a = mRefreshableView.getContentHeight() * mRefreshableView.getScale();
        //数学函数，求一个浮点数的地板，就是求一个最接近它的整数，它的值小于或等于这个浮点数。
        float exactContentHeight = (float) Math.floor(a);
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());

    }
}
