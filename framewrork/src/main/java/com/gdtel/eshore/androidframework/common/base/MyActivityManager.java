package com.gdtel.eshore.androidframework.common.base;

import java.util.Stack;

import android.app.Activity;
/**
 * 界面管理类
 * 用单例模式来管理在栈里的Activity
 * @author Linjiaodu
 *
 */
public class MyActivityManager {
	//存放Activity的栈
	private static Stack<Activity> mActivityStack;

	private static MyActivityManager instance;


	private MyActivityManager(){

	}
	/**
	 * 获取MyActivityManager的对象
	 * @return MyActivityManager的对象
	 */
	public static MyActivityManager getMyActivityManager(){
		if(instance == null){
			instance = new MyActivityManager();
		}
		return instance;
	}

	public void pop(){
		//获取栈顶Activity
		Activity activity =mActivityStack.lastElement();
		if(activity!=null){
			mActivityStack.pop();
		}
	}

	/**
	 * pop出Activity,但是不finish
	 * @param activity
	 */
	public void pop(Activity activity){
		//将指定的Activity Finnish
		if(activity!=null){  
			mActivityStack.remove(activity);  
		}  
	}

	/**
	 * pop出栈顶，相当于finnish()
	 * @param activity
	 */
	public void popActivity(){
		//获取栈顶Activity
		Activity activity =mActivityStack.lastElement();
		if(activity!=null){
			//如果Activity不为空则Finnish掉
			activity.finish();  
			activity=null;  
			mActivityStack.pop();
		}
	}

	/**
	 * pop出Activity，相当于finnish()
	 * @param activity
	 */
	public void popActivity(Activity activity){
		//将指定的Activity Finnish
		if(activity!=null){  
			activity.finish();  
			mActivityStack.remove(activity);  
			activity=null;  
		}  
	}

	/**
	 * 添加Activity
	 * @param activity
	 */
	public void pushActivity(Activity activity){  
		if(mActivityStack==null){  
			mActivityStack=new Stack<Activity>();  
		}  
		mActivityStack.add(activity);  
	}  

	/*public void popAllActivityExceptOne(Class cls){  
        while(mActivityStack.size() > 1){  
            Activity activity=currentActivity();  
            if(activity==null){ 
            	mActivityStack.pop();
                break;  
            }  
            if(activity.getClass().equals(cls) ){  
            }else{
            	popActivity(activity);  
            }

        }  
    }*/

	public void popAllActivity(){  
		while(!mActivityStack.isEmpty()){  
			Activity activity=currentActivity();  
			if(activity==null){ 
				mActivityStack.pop();
				break;  
			}  
			popActivity();  

		}  
	}

	public boolean isEmpty(){
		return mActivityStack.isEmpty();
	}

	public Activity currentActivity(){  
		Activity activity=mActivityStack.lastElement();  
		return activity;  
	}
	public static Stack<Activity> getmActivityStack() {
		return mActivityStack;
	}
	
	
}
