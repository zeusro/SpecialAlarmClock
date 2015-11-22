package com.gdtel.eshore.demo.ui.shake;

import android.app.Activity;
import android.content.Intent;

import com.gdtel.eshore.anroidframework.R;

public class ActivityJumpManager {
	private int curr_jump_model_start = RIGHT_TO_LEFT;
	private int curr_jump_model_back = BOTTOM_TO_TOP;
	
	public final static int RIGHT_TO_LEFT = 0x0;
	public final static int BOTTOM_TO_TOP = 0x2;
	public final static int ZOOM_IN = 0x4;
	public final static int LEFT_TO_RIGHT = 0x1;
	public final static int TOP_TO_BOTTOM = 0x3;
	public final static int ZOOM_OUT = 0x5;
	
	/**
	 * 跳转页面效果
	 * @param act
	 * @param cls
	 * @param model	只包括[RIGHT_TO_LEFT BOTTOM_TO_TOP ZOOM_IN]	默认RIGHT_TO_LEFT
	 */
	public static void MoveIn(Activity act, Class<?> cls, int model){
		act.startActivity(new Intent(act, cls)); 
		switch (model) {
		case RIGHT_TO_LEFT:
			act.overridePendingTransition(R.anim.mi_right_to_left, R.anim.mo_right_to_left);
			break;
		case BOTTOM_TO_TOP:
			//TODO   参考R.anim.left_to_right
			break;
		case ZOOM_IN:
			act.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;
		default:
			act.overridePendingTransition(R.anim.mi_right_to_left, R.anim.mo_right_to_left);
			break;
		}
	}
	
	/**
	 * 页面返回效果
	 * @param act
	 * @param model	只包括[LEFT_TO_RIGHT TOP_TO_BOTTOM ZOOM_OUT]	默认LEFT_TO_RIGHT
	 */	
	public static void MoveOut(Activity act, Class<?> cls, int model){
		act.startActivity(new Intent(act, cls));

		switch (model) {
		case LEFT_TO_RIGHT:
			act.overridePendingTransition(R.anim.mi_left_to_right, R.anim.mo_left_to_right);
			break;
		case TOP_TO_BOTTOM:
			//TODO   参考R.anim.left_to_right
			break;
		case ZOOM_OUT:
			act.overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
			break;
		default:
			act.overridePendingTransition(R.anim.mi_left_to_right, R.anim.mo_left_to_right);
			break;
		}
	}
}
