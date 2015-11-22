package com.gdtel.eshore.androidframework.ui.view.scroller;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.gdtel.eshore.androidframework.common.base.TaskCallBack;

/**
 * 
 * Copyright 2015 All right reserved
 * 
 * @Author: Wudl
 * @Date: 2015年8月5日
 * @Description: 滑动ViewGroup
 */
public class ScrollViewGroup extends ViewGroup{

	private Context mContext;
	private static String TAG = "ScrollViewGroup";
	private Scroller mScroller = null;
	private int currentScreen = 0; // 当前屏

	// 触摸
	public static int SNAP_VELOCITY = 600;
	private int mTouchSlop = 0;
	private float mLastionMotionX = 0;
	private float mLastMotionY = 0;
	// 触摸速率
	private VelocityTracker mVelocityTracker = null;

	// 滑动的状态
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private int mTouchState = TOUCH_STATE_REST;
	
	//回调
	private TaskCallBack<Integer> callBack;

	public ScrollViewGroup(Context context) {
		super(context);

		this.mContext = context;
		init();
	}

	public ScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		this.mContext = context;
		init();
	}

	public ScrollViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context;
		init();
	}

	public void init(/*ListView listView*/) {
		mScroller = new Scroller(mContext);
//		addView(listView);
		// 初始化一个最小滑动距离
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

//		Log.i(TAG, "--- onLayout --");
		int startLeft = 0; // 每个子视图的起始布局坐标
		int startTop = 0; // 
		int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);

			// 判断子布局是否可见
			if (child.getVisibility() != View.GONE)
				child.layout(startLeft, startTop, startLeft + getWidth(),
						startTop + getHeight());

			startLeft = startLeft + getWidth(); // 校准每个子View的起始布局位置
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

//		Log.e(TAG, "onInterceptTouchEvent");

		final int action = ev.getAction();
		// 当前滑动状态不为TOUCH_STATE_REST，并且是move动作，不进行处理
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
//			Log.e(TAG, "onInterceptTouchEvent move");
			final int xDiff = (int) Math.abs(mLastionMotionX - x);
			// 大于最小滑动距离
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;

		case MotionEvent.ACTION_DOWN:
//			Log.e(TAG, "onInterceptTouchEvent down");
			mLastionMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;

			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
//			Log.e(TAG, "onInterceptTouchEvent up or cancel");
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//		Log.i(TAG, "--- onMeasure --");

		// 设置该ViewGroup的大小
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);

		int childCount = getChildCount();
//		Log.i(TAG, "--- onMeasure childCount is -->" + childCount);
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			// 设置每个子视图的大小 
			child.measure(getWidth(), getHeight());
		}
	}

	
	@Override
	public void computeScroll() {

		if (callBack != null) {
			this.callBack.callBackResult(currentScreen);
		}
//		Log.e(TAG, "computeScroll");
		if (mScroller.computeScrollOffset()) {
			// 每次移动到手指当前的坐标
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

			// 刷新View
			postInvalidate();
		} else{
//			Log.i(TAG, "the scoller already done");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

//		Log.i(TAG, "--- onTouchEvent-- ");

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}

		mVelocityTracker.addMovement(event);

		super.onTouchEvent(event);

		// 手指坐标
		float x = event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			Log.e(TAG, "MotionEvent.ACTION_MOVE");
			// 如果屏幕的动画还没结束，屏幕有按下的动作，强制结束该动画
			if (mScroller != null) {
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
			}

			mLastionMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int detaX = (int) (mLastionMotionX - x);
			scrollBy(detaX, 0);

//			Log.e(TAG, "MotionEvent.ACTION_MOVE");
			mLastionMotionX = x;

			break;
		case MotionEvent.ACTION_UP:

			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);

			int velocityX = (int) velocityTracker.getXVelocity();

//			Log.e(TAG, "MotionEvent.ACTION_UP");

			// 向右滑动速率大于SNAP_VELOCITY，进行切屏处理
			if (velocityX > SNAP_VELOCITY && currentScreen > 0) {
				// Fling enough to move left
				snapToScreen(currentScreen - 1);
			}
			// 向左滑动速率大于SNAP_VELOCITY，进行切屏处理
			else if (velocityX < -SNAP_VELOCITY
					&& currentScreen < (getChildCount() - 1)) {
//				Log.e(TAG, "snap right");
				snapToScreen(currentScreen + 1);
			}
			//滑动速率小于SNAP_VELOCITY，效果看起来是随着手指滑动的
			else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}

			mTouchState = TOUCH_STATE_REST;

			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	/**
	 * 
	 * Copyright 2015 All right reserved
	 * 
	 * @Type:void
	 * @Author: Wudl
	 * @Date: 2015年8月5日
	 * @Description: TODO
	 */
	private void snapToScreen(int whichScreen) {

		currentScreen = whichScreen;

		if (currentScreen > getChildCount() - 1)
			currentScreen = getChildCount() - 1;

		int dx = currentScreen * getWidth() - getScrollX();

		mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx) * 2);

		// 刷新View,重绘界面
		invalidate();

	}

	/**
	 * 
	 * Copyright 2015 All right reserved
	 * 
	 * @Name: ScrollViewGroup.java
	 * @Author: Wudl
	 * @Date: 2015年8月5日
	 * @Description:判断滑动到哪一屏，决定向左还是向右
	 */
	private void snapToDestination() {
		// 当前的偏移位置
		int scrollX = getScrollX();

		int destScreen = (scrollX + getWidth() / 2) / getWidth();

		snapToScreen(destScreen);
	}

	
	public void setTaskCallBack(TaskCallBack<Integer> callBack) {

		this.callBack = callBack;
	}

}
