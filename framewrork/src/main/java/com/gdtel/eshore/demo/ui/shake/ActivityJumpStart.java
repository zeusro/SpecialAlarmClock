package com.gdtel.eshore.demo.ui.shake;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.gdtel.eshore.anroidframework.R;

public class ActivityJumpStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jump_start);
	}
	
	public void onClickR2L_SatrtAct(View v){
		ActivityJumpManager.MoveIn(this, ActivityJumpBack.class, ActivityJumpManager.RIGHT_TO_LEFT);
	}
	
	@Override
	public void onBackPressed() {
		ActivityJumpManager.MoveOut(this, MainActivity.class, ActivityJumpManager.ZOOM_OUT);
	}
}
