package com.gdtel.eshore.demo.ui.shake;

import com.gdtel.eshore.anroidframework.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ActivityJumpBack extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jump_back);
	}
	
	public void onClickBack(View v){
		ActivityJumpManager.MoveOut(this, ActivityJumpStart.class, ActivityJumpManager.RIGHT_TO_LEFT);
	}

}
