package zeusro.specialalarmclock.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import zeusro.specialalarmclock.R;

/**
 * Created by Z on 2015/11/27.
 */

public class SlideView extends View {

    public interface SlideListener {
        void onDone();
    }

    private static final int MSG_REDRAW = 1;
    private static final int DRAW_INTERVAL = 50;
    private static final int STEP_LENGTH = 5;

    private Paint mPaint;
    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;
    private LinearGradient mGradient;
    private int[] mGradientColors;
    private int mGradientIndex;
    private Interpolator mInterpolator;
    private SlideListener mSlideListener;
    private float mDensity;
    private Matrix mMatrix;
    private ValueAnimator mValueAnimator;

    /**
     * 背景字体
     */
    private String mText;
    private int mTextSize;
    private int mTextLeft;
    private int mTextTop;

    private int mSlider;
    private Bitmap mSliderBitmap;
    private int mSliderLeft;
    private int mSliderTop;
    private Rect mSliderRect;
    private int mSlidableLength;    // SlidableLength = BackgroundWidth - LeftMagins - RightMagins - SliderWidth
    private int mEffectiveLength;   // Suggested length is 20pixels shorter than SlidableLength
    private float mEffectiveVelocity;

    private float mStartX;
    private float mStartY;
    private float mLastX;
    private float mMoveX;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REDRAW:
                    mMatrix.setTranslate(mGradientIndex, 0);
                    mGradient.setLocalMatrix(mMatrix);
                    invalidate();
                    mGradientIndex += STEP_LENGTH * mDensity;
                    if (mGradientIndex > mSlidableLength) {
                        mGradientIndex = 0;
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
                    break;
            }
        }
    };

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
        mInterpolator = new AccelerateDecelerateInterpolator();
        mDensity = getResources().getDisplayMetrics().density;
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.SlideView);
        mText = typeArray.getString(R.styleable.SlideView_maskText);
        mTextSize = typeArray.getDimensionPixelSize(R.styleable.SlideView_maskTextSize, R.dimen.mask_text_size);
        mTextLeft = typeArray.getDimensionPixelSize(R.styleable.SlideView_maskTextMarginLeft, R.dimen.mask_text_margin_left);
        mTextTop = typeArray.getDimensionPixelSize(R.styleable.SlideView_maskTextMarginTop, R.dimen.mask_text_margin_top);

        mSlider = typeArray.getResourceId(R.styleable.SlideView_slider, R.mipmap.app_icon);
        mSliderLeft = typeArray.getDimensionPixelSize(R.styleable.SlideView_sliderMarginLeft, R.dimen.slider_margin_left);
        mSliderTop = typeArray.getDimensionPixelSize(R.styleable.SlideView_sliderMarginTop, R.dimen.slider_margin_top);
        mSliderBitmap = BitmapFactory.decodeResource(getResources(), mSlider);
        mSliderRect = new Rect(mSliderLeft, mSliderTop, mSliderLeft + mSliderBitmap.getWidth(), mSliderTop + mSliderBitmap.getHeight());

        mSlidableLength = typeArray.getDimensionPixelSize(R.styleable.SlideView_slidableLength, R.dimen.slidable_length);
        mEffectiveLength = typeArray.getDimensionPixelSize(R.styleable.SlideView_effectiveLength,
                R.dimen.effective_length);
        mEffectiveVelocity = typeArray.getDimensionPixelSize(R.styleable.SlideView_effectiveVelocity,
                R.dimen.effective_velocity);
        typeArray.recycle();

        mGradientColors = new int[]{Color.argb(255, 120, 120, 120), Color.argb(255, 120, 120, 120), Color.argb(255, 255, 255, 255)};
        mGradient = new LinearGradient(0, 0, 100 * mDensity, 0, mGradientColors, new float[]{0, 0.7f, 1}, TileMode.MIRROR);
        mGradientIndex = 0;
        mPaint = new Paint();
        mMatrix = new Matrix();
        mPaint.setTextSize(mTextSize);
        mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
    }

    public void setSlideListener(SlideListener slideListener) {
        mSlideListener = slideListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setShader(mGradient);
        canvas.drawText(String.valueOf(mText), mTextLeft, mTextTop, mPaint);
        // Slider's moving rely on the mMoveX.
        canvas.drawBitmap(mSliderBitmap, mSliderLeft + mMoveX, mSliderTop, null);
    }

    public void reset() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        mMoveX = 0;
        mPaint.setAlpha(255);
        mHandler.removeMessages(MSG_REDRAW);
        mHandler.sendEmptyMessage(MSG_REDRAW);
    }

    /**
     * 碰瓷时触发
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If the start point is not on the slider, moving slider will not be executed.
        if (event.getAction() != MotionEvent.ACTION_DOWN
                && !mSliderRect.contains((int) mStartX, (int) mStartY)) {
            if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
            }
            return super.onTouchEvent(event);
        }
        acquireVelocityTrackerAndAddMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                mLastX = mStartX;
                mHandler.removeMessages(MSG_REDRAW);
                break;

            case MotionEvent.ACTION_MOVE:
                mLastX = event.getX();
                if (mLastX > mStartX) { // Can not exceed the left boundary, otherwise, mMoveX will get a minimum value.
                    // The transparency of text will be changed along with moving slider
                    int alpha = (int) (255 - (mLastX - mStartX) * 3 / mDensity);
                    if (alpha > 1) {
                        mPaint.setAlpha(alpha);
                    } else {
                        mPaint.setAlpha(0);
                    }
                    // Can not exceed the right boundary, otherwise, mMoveX will get a maximum value.
                    if (mLastX - mStartX > mSlidableLength) {
                        mLastX = mStartX + mSlidableLength;
                        mMoveX = mSlidableLength;
                    } else {
                        mMoveX = (int) (mLastX - mStartX);
                    }
                } else {
                    mLastX = mStartX;
                    mMoveX = 0;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                float velocityX = mVelocityTracker.getXVelocity();
                if (mLastX - mStartX > mEffectiveLength || velocityX > mEffectiveVelocity) {
                    startAnimator(mLastX - mStartX, mSlidableLength, velocityX, true);
                } else {
                    startAnimator(mLastX - mStartX, 0, velocityX, false);
                    mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
                }
                releaseVelocityTracker();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 动画
     *
     * @param start         开始的坐标
     * @param end           结束点的坐标
     * @param velocity
     * @param isRightMoving
     */
    private void startAnimator(float start, float end, float velocity, boolean isRightMoving) {
        if (velocity < mEffectiveVelocity) {
            velocity = mEffectiveVelocity;
        }
        int duration = (int) (Math.abs(end - start) * 1000 / velocity);
        mValueAnimator = ValueAnimator.ofFloat(start, end);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMoveX = (Float) animation.getAnimatedValue();
                int alpha = (int) (255 - (mMoveX) * 3 / mDensity);
                if (alpha > 1) {
                    mPaint.setAlpha(alpha);
                } else {
                    mPaint.setAlpha(0);
                }
                invalidate();
            }
        });
        mValueAnimator.setDuration(duration);
        mValueAnimator.setInterpolator(mInterpolator);
        if (isRightMoving) {
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mSlideListener != null) {
                        mSlideListener.onDone();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        mValueAnimator.start();
    }

    private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
