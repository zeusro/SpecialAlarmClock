package com.eshore.yourapp.activity.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdtel.eshore.androidframework.common.util.AppConstant;
import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
import com.gdtel.eshore.anroidframework.R;
import com.gdtel.eshore.anroidframework.R.array;
import com.gdtel.eshore.anroidframework.R.drawable;
import com.gdtel.eshore.anroidframework.R.id;
import com.gdtel.eshore.anroidframework.R.layout;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {
	List<Intent> intents = new ArrayList<Intent>();
	private LinearLayout main_bottom_bar,content_view;
	private ArrayList<View> pageViews = new ArrayList<View>();
	private int currIndex = 0;
	public static final String TAB = "tab_";
	private static final String TAG = "ViewDataMainActivity";
	private View[] mTabs;
	List<String> names = new ArrayList<String>() ;
	List<String> iconIds = new ArrayList<String>() ;
	List<String> clzName = new ArrayList<String>() ;
	private Resources res;
	int screenHeight  ;
	int screenWidth  ;
	private int one;// 单个水平动画位移
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main_app_home);
		super.onCreate(savedInstanceState);
		res = getResources();
		screenWidth = AppConstant.SCREENWIDTH;
        screenHeight = AppConstant.SCREENHEIGHT;
		initView();
		changeTabBg(0);
		//版本更新
//		UpgradeUtils.checkNewVersion(this);
	}
	/**
	 * 切换背景
	 * <功能详细描述>
	 * @param i
	 * @see [类、类#方法、类#成员]
	 */
	private void changeTabBg(int index) {

		content_view.removeAllViews();
		currIndex = index;
		content_view.addView(pageViews.get(currIndex));
		for(int i=0;i<mTabs.length;i++){
			if(index==i){
				((LinearLayout)mTabs[i].findViewById(R.id.item_layout)).setBackgroundResource(R.mipmap.bottom_menu_selected);
//				mTabs[i].setBackgroundResource(R.mipmap.bottom_menu_selected);
			}else{
				((LinearLayout)mTabs[i].findViewById(R.id.item_layout)).setBackgroundColor(0x0);
//				mTabs[i].setBackgroundColor(0x0);
			}
		}
	}

	/**
	 * 初始内容界面。
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void initView() {

		clzName = Arrays.asList(res.getStringArray(R.array.operational_navigationbar_class_name));
		if (clzName==null||clzName.size()<=0) return;
		Bundle mBundle = new Bundle();//getIntent().getExtras();
		for (int i = 0; i < clzName.size(); i++) {
			try {
				intents.add(new Intent(this, Class.forName(clzName.get(i))).putExtras(mBundle));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		main_bottom_bar = (LinearLayout)findViewById(R.id.main_bottom_bar);
		content_view = (LinearLayout)findViewById(R.id.content_view);
		names = Arrays.asList(res.getStringArray(R.array.operational_navigationbar_names));
		iconIds = Arrays.asList(res.getStringArray(R.array.operational_navigationbar_icons));
		initBottomBar(names,iconIds);
		initContentView(names);
	}

	/**
	 * 初始化内容
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void initContentView(List<String> names) {

		content_view.removeAllViews();
		for(int i=0;i<intents.size();i++){
				Intent intent = intents.get(i);
				intent.putExtra(AppConstant.APP_ITEM_TITLE, names.get(i));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				LocalActivityManager manager = this.getLocalActivityManager();
				final Window w = manager.startActivity(TAB+i, intent);
				View currentView = w.getDecorView();
				pageViews.add(currentView);
			}
		content_view.addView(pageViews.get(currIndex));
	}

	/**
	 * 初始化底部菜单
	 * <功能详细描述>
	 * @param names2
	 * @param iconIds2
	 * @see [类、类#方法、类#成员]
	 */
	private void initBottomBar(List<String> names2, List<String> iconIds2) {

		main_bottom_bar.removeAllViews();
		if (names==null||names.size()<=0||iconIds==null||iconIds.size()<=0) return;
		int len = names.size();
		int itemWidth , itemHeight;
		itemWidth = (screenWidth/len);
		itemHeight = screenWidth/len;
		one = screenWidth / len; // 设置水平动画平移大小
		DebugLog.d(TAG, "initBottomBar--:itemWidth ="+itemWidth+" itemHeight="+itemHeight+" one="+one);
		mTabs = new View[len];
		for(int i=0;i<len;i++){
			View item_view = LayoutInflater.from(this).inflate(R.layout.bar_item_view, null);
			item_view.setLayoutParams(new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT));
			ImageView imageView = (ImageView)item_view.findViewById(R.id.item_img);
			TextView textView = (TextView)item_view.findViewById(R.id.item_name);
			textView.setText(names.get(i));
//			imageView.setBackgroundResource(res.getIdentifier(iconIds.get(i), "drawable", getPackageName()));
			imageView.setImageResource(res.getIdentifier(iconIds.get(i), "drawable", getPackageName()));
			if (item_view.getParent()!=null&&item_view.getParent() instanceof ViewGroup) {
				((ViewGroup)item_view.getParent()).removeView(item_view);
			}
			item_view.setOnClickListener(new ItemOnClickListener(i));
			mTabs[i] = item_view;
			main_bottom_bar.addView(item_view);
		}
	}
	
	public class ItemOnClickListener implements OnClickListener{
		private int index ;
		public ItemOnClickListener(int index) {
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			changeTabBg(index);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
