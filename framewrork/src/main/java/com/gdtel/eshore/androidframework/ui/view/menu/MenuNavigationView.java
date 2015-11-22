package com.gdtel.eshore.androidframework.ui.view.menu;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.gdtel.eshore.anroidframework.R;

/**
 * 导航控件的父控件
 * 有层级关系的控件都继承该类
 * 需扩展
 * @author Administrator
 *
 */
public abstract class MenuNavigationView extends LinearLayout {
	public enum NavigationViewType{
		HORIZONTAL,
		VERTICAL
	}
	public enum NavigationName{
		MENU,		//菜单
		PROVINCE,	//省市区机构树
		ZQJL,		//政企激励机构树
		LZFZ,		//揽装发展机构树
	}

	protected final static String tag = "AdamNavigationView";
	protected Context mContext;
	protected LayoutInflater inflater;
	protected ArrayList<NavigationHodler> list_holder;
	
	protected RelativeLayout adam_navigation_main_layout;
	protected LinearLayout adam_navigation_close_layout;
	
	protected final int first_level = 1;
	protected int layout_item_width /*  单项宽度 */, layout_small_width/* 压缩单项宽度 */;
	protected int min_item_count;
	protected int layout_item_height;		//单项高度
	protected NavigationViewType mViewType = NavigationViewType.VERTICAL;		//导航是横屏或是竖屏
	protected NavigationName navigation_name = NavigationName.MENU;				//导航名
	protected int limit_level = -1;			//限制下转层级		-1:没有限制	其他层级从0开始
	
	protected String navigation_step;
	private ProgressDialog mDialog;
	
	public MenuNavigationView(Context context) {
		super(context);
		initView(context);
	}
	
	public MenuNavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private void initView(Context context){
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_adam_navigation_view, null);
		adam_navigation_main_layout = (RelativeLayout) view.findViewById(R.id.adam_navigation_main_layout);
		adam_navigation_close_layout = (LinearLayout) view.findViewById(R.id.adam_navigation_close_layout);
		
		this.addView(view);
		
		list_holder = new ArrayList<NavigationHodler>();
		DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
		layout_item_width = dm.widthPixels*2/5 - dip2px(15);
		layout_item_height = dip2px(38);
		layout_small_width = dip2px(46);
	}
	
	/**
	 * 显示关闭按钮layout
	 */
	public void setCloseLayoutVisible(){
		adam_navigation_close_layout.setVisibility(View.VISIBLE);
		((ImageView)findViewById(R.id.adam_navigation_close_img))
			.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				MenuNavigationView.this.setVisibility(View.GONE);
			}
		});
	}
	
	protected int dip2px(float dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 添加一层菜单
	 * @return
	 */
	protected NavigationHodler addNavigationHolder(){
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_adam_layer_item, null);
		adam_navigation_main_layout.addView(layout, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		LinearLayout layer_layout = 
				(LinearLayout) layout.findViewById(R.id.adam_linearlayout);
		
		NavigationHodler holder = new NavigationHodler();
		holder.linearLayout = layout;
		holder.layer_layout = layer_layout;
		
		list_holder.add(holder);
		
		return holder;
	}
	
	protected class NavigationHodler {
		LinearLayout linearLayout;
		LinearLayout layer_layout;
	}
	
	/**
	 * 保存选择的步骤
	 * @param level
	 * @param resourceId
	 */
	protected void setNavigationStep(int level, String resourceId){
		if(level == 0){
			navigation_step = resourceId;
		}else{
			String[] steps = navigation_step.split("\\|");
			if(steps.length == level){
				navigation_step += "|" + resourceId;
			}else{
				navigation_step = "";
				for(int i=0;i<level;i++){
					navigation_step += steps[i] + "|";
				}
				navigation_step += resourceId;
			}
		}
//		DebugLog.d(tag, "navigation_step:" + navigation_step + ",这次点击的是:" + resourceId);
	}
	
	/**
	 * 清除菜单数据
	 */
	public void clearData(){
		if(list_holder != null) {
			list_holder.clear();
			list_holder = null;
		}
	};
	
	/**
	 * 设置每层菜单的宽度
	 * @param which
	 * @param add_layout
	 */
	protected void setViewWidth(int which, NavigationHodler add_layout) {
		// 设置宽度
		if (list_holder.size() == 1) {
			android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) list_holder
					.get(0).linearLayout.getLayoutParams();
			if(navigation_name == NavigationName.MENU){
				lp.width = android.widget.FrameLayout.LayoutParams.MATCH_PARENT;
			}else{
				lp.width = layout_item_width;
			}
			lp.leftMargin = 0;
			list_holder.get(0).linearLayout.setLayoutParams(lp);
			return;
		}
		boolean is_focus = false;
		int count_40dp = 0;
		for (int i = 0; i < list_holder.size(); i++) {
			if (list_holder.get(i).linearLayout.getVisibility() == View.GONE) {
				break;
			}
			int layout_width = 0;
			int layout_lm = 0;
			int less_count = mViewType == NavigationViewType.VERTICAL ? 2 : 4;
			if (which < less_count) {
				layout_width = layout_item_width;
				layout_lm = i * layout_item_width;
			} else {
				if (which - less_count - i >= 0) {
					layout_width = layout_small_width;
					layout_lm = count_40dp * layout_small_width;
					count_40dp++;
				} else {
					layout_width = layout_item_width;
					layout_lm = count_40dp * layout_small_width
							+ (i - count_40dp) * layout_item_width;
				}
			}
//			DebugLog.d(tag, "第" + i + "层菜单======layout_width:" + layout_width + "====layout_lm:" + layout_lm);
			if (list_holder.get(i).equals(add_layout)) {
				is_focus = true;
				android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) list_holder
						.get(i).linearLayout.getLayoutParams();
				lp.width = layout_width;
				lp.leftMargin = layout_lm;
				list_holder.get(i).linearLayout.setLayoutParams(lp);
				continue;
			}
			if (!is_focus) {
				android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) list_holder
						.get(i).linearLayout.getLayoutParams();
				lp.width = layout_width;
				lp.leftMargin = layout_lm;
				list_holder.get(i).linearLayout.setLayoutParams(lp);
			}
		}
	}
	
	public void buildProgressDialog(Context context){
		if(mDialog==null){
			mDialog = new ProgressDialog(context);
			mDialog.setMessage("数据加载中...");
			mDialog.setCancelable(true);
		}
	}
	
	public void showDialog(){
		if (mDialog!=null&&!mDialog.isShowing()) {
			mDialog.show();
		}
	}
	
	public void showDialog(String msg){
		if (mDialog!=null&&!mDialog.isShowing()) {
			mDialog.setMessage(msg);
			mDialog.setCancelable(true);
			mDialog.show();
		}
	}
	
	public void dismissDialog(){
		if (mDialog!=null&&mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
	
	/**
	 * 设置菜单的层数限制
	 * @param limit
	 */
	public void setLimitLevel(int limit){
		this.limit_level = limit;
	}
	
	public int getLimitLevel(){
		return this.limit_level;
	}
	
	public abstract void setNavigationName(NavigationName name);
	public abstract void onNextEvent(Object o);
	public abstract void onFinishEvent(Object o);
	
	//=================================================================================
	/**
	 * 菜单点击触发事件
	 * @author Administrator
	 *
	 */
	public static interface MenuNavigationListener{
		public void onMenuNavigationEvent(String menu_id, String menu_pid, String menu_name);
	}
}
