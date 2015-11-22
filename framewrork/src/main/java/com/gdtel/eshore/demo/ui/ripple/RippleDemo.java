package com.gdtel.eshore.demo.ui.ripple;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.gdtel.eshore.androidframework.ui.view.ripple.RippleView;

public class RippleDemo extends Activity {
	RippleView	m_rippleView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        m_rippleView = new RippleView(this);
        setContentView(m_rippleView);
    }
    
    public boolean onTouchEvent(MotionEvent event) 
    {
    	m_rippleView.processTouchEvent(event);

    	return super.onTouchEvent(event);
    }
	
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	m_rippleView.processKeyDown(keyCode, event);
    	
    	return super.onKeyDown(keyCode, event);
    }
    
	protected void onStop()
	{
		m_rippleView.stop();
		
		super.onStop();    	
	}
	
	protected void onResume() {
		m_rippleView.resume();
		
		super.onResume();
	}
	
	protected void onDestroy() {
		m_rippleView.destroy();
		m_rippleView = null;
		
    	super.onDestroy();   	
    }
}