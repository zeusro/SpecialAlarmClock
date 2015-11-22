package com.gdtel.eshore.demo.ui.shake;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.gdtel.eshore.anroidframework.R;

public class MainActivity extends Activity {
	
	private LinearLayout shake_layout;
	private Animation shake;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_shake);
		
		shake_layout = (LinearLayout) findViewById(R.id.shake_layout);
		shake = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件
	}
	
	public void onClickShake(View v){
		shake_layout.startAnimation(shake);
	}
	
	public void onClickStartActivity(View v){
		ActivityJumpManager.MoveIn(this, ActivityJumpStart.class, ActivityJumpManager.ZOOM_IN);
	}

}
