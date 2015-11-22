package com.gdtel.eshore.androidframework.ui.view.scroller;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
/**
 * 弹性view
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2014-5-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ElasticScrollView extends ScrollView {
	private View inner;
	private float y;
	private Rect normal = new Rect();;

	public ElasticScrollView(Context context) {
		super(context);
	}
	
	public ElasticScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d("onInterceptTouchEvent", "---onInterceptTouchEvent--"+ev.getAction());
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("onTouchEvent", "---onTouchEvent--"+ev.getAction());
		if (inner == null) {
			return super.onTouchEvent(ev);
		} else {
			commOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			y = ev.getY();
			Log.d("ACTION_DOWN", "==y="+y);
			break;
		case MotionEvent.ACTION_UP:
			if (isNeedAnimation()) {
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float preY = y;
			float nowY = ev.getY();
			int deltaY = (int) (preY - nowY);
			// ��
			scrollBy(0, deltaY);

			y = nowY;
			if (isNeedMove()) {
				if (normal.isEmpty()) {
					normal.set(inner.getLeft(), inner.getTop(), inner
							.getRight(), inner.getBottom());

				}
				inner.layout(inner.getLeft(), inner.getTop() - deltaY, inner
						.getRight(), inner.getBottom() - deltaY);
			}
			break;
		default:
			break;
		}
	}

	// ������ƶ�

	public void animation() {
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(200);
		inner.startAnimation(ta);
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();
	}

	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = inner.getMeasuredHeight() - getHeight();
		Log.d("isNeedMove", "------inner.getMeasuredHeight()="+inner.getMeasuredHeight()+"------inner.getHeight()="+inner.getHeight()+"------offset="+offset);
		Log.d("isNeedMove", "------getMeasuredHeight()="+getMeasuredHeight()+"------getHeight()="+getHeight()+"------offset="+offset);
		int scrollY = getScrollY();
		Log.d("isNeedMove", "------scrollY="+scrollY);
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}
}
