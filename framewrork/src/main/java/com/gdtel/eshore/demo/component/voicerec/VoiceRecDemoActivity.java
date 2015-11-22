package com.gdtel.eshore.demo.component.voicerec;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdtel.eshore.androidframework.component.voicerec.VoiceRecActivity;
import com.gdtel.eshore.anroidframework.R;
/**
 * 语音识别
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2014-11-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class VoiceRecDemoActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 200;
	private TextView mTextView ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		
		mTextView = (TextView) findViewById(R.id.result); 
		
		Button mButton = (Button) findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(VoiceRecDemoActivity.this, VoiceRecActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
	}
	
	/**
	 * 展示结果
	 * {@inheritDoc}
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				mTextView.setText(bundle.getString("result"));
			}
			break;
		}
    }	

}
