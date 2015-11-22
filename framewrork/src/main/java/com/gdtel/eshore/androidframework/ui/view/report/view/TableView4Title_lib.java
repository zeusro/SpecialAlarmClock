package com.gdtel.eshore.androidframework.ui.view.report.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gdtel.eshore.androidframework.ui.view.report.entity.TableHeadEntity;
import com.gdtel.eshore.androidframework.ui.view.report.entity.TableInfoRes;
import com.gdtel.eshore.androidframework.ui.view.report.view.TableView4ScrollView_lib.OnScrollChangedListener_Test;
import com.gdtel.eshore.anroidframework.R;

/**
 * adam  0730表头已完成
 * @author Administrator
 *
 */
public class TableView4Title_lib extends LinearLayout{
	public final static int UNITY_TABLE_QD = 999;
	public static String UNITY_TABLE_QD_NAME = "";
	
	private final static String tag = TableView4Title_lib.class.getSimpleName();
	
	private Context mContext;
	/** 标题控件. */
	private View table_title_layout;
	private LinearLayout table_main_layout;
	private TableInfoRes table_total_info;		//报表表头
	private ListView table_content_lv;
	private TableView4ScrollView_lib table_title_scrollview;
	
	private TaskCallBack<Integer> callBack;
	private List<CharSequence[]> table_data;	//报表数据
	private List<ArrayList<Integer>> typeList;

	private int screen_width = 1080;
	private int screen_height = 1920;
	private int table_cell_max = screen_width / 4;	//报表单元格宽度最大值
	private int table_cell_width = 0;	//报表单元格宽度
	private int table_cell_heigth = 0;	//报表单元格高度
	private int table_title_const_height = 0;	//报表标题单元格固定高度
	private float table_font_size;		//报表字体大小
	
	private int fix_cols_num = -1;		//锁定列数
	private int table_title_cols_count;	//表头总列数(包括可滚动和锁定的)
	
	private String report_id = "";
	
	/** 标题字体颜色. */
	private int titleTextColor = Color.argb(255, 100, 100, 100);
	/** 内容字体颜色. */
	private int contentTextColor = Color.argb(255, 100, 100, 100);
	/**  */
	private int[] rows_background_color = 
			new int[]{Color.parseColor("#e6f5ff"),Color.parseColor("#faf5f0"), Color.parseColor("#f0f9f0")};
	
	/** zlevel与level是否相同		(不相同  可下钻)*/
	private boolean zlevel_level = false;
	/** 可下钻的列数    */
	private int zlevel_index = -1;
	
	/** qdlevel与level是否相同	(相同 是清单 可下钻) */
	private boolean qdlevel_level = false;
	/** 可下钻的列数    */
	private int qdlevel_index = -1;
	/** 当前页数 */
	private int curr_page = 1;
	private boolean isLastRow = false;		//是否到达底部

	public TableView4Title_lib(Context context, TableInfoRes table_info_res
			, List<CharSequence[]> data, TaskCallBack<Integer> callBack
			, List<ArrayList<Integer>> typeList) {
		super(context);
		
		this.mContext = context;
		this.table_total_info = table_info_res;
		this.table_data = data;
		this.callBack = callBack;
		this.typeList = typeList;
		table_font_size = context.getResources().getDimension(R.dimen.table_font_size);
		table_cell_heigth = screen_height/16;
		table_title_const_height = table_cell_heigth;
//		DebugLog.d(tag, "table_font_size : " + table_font_size);
		
		// 设定纵向布局
		setOrientation(VERTICAL);
		// 设定背景为白色
		setBackgroundColor(Color.WHITE);
		
		// 初始化listview
		table_content_lv = new ListView(context);
		table_content_lv.setFadingEdgeLength(0);
		table_content_lv.setCacheColorHint(0);
		table_content_lv.setDivider(null);
		table_content_lv.setDividerHeight(0);
//		table_content_lv.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		table_content_lv.setOnTouchListener(null);
		table_content_lv.setLayoutParams(
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT
						, LinearLayout.LayoutParams.MATCH_PARENT));
	}
	
	public int getScreen_width() {
		return screen_width;
	}

	/**
	 * 设置屏幕宽度()
	 * @param screen_width
	 */
	public void setScreen_width(int screen_width) {
		this.screen_width = screen_width;
		table_cell_max = screen_width / 4;	//报表单元格宽度最大值
//		LayoutParams ll = (LayoutParams) table_content_lv.getLayoutParams();
//		ll.width = this.screen_width;
	}

	public int getScreen_height() {
		return screen_height;
	}

	/**
	 * 设置屏幕高度
	 * @param screen_height
	 */
	public void setScreen_height(int screen_height) {
		this.screen_height = screen_height;
		table_cell_heigth = screen_height/16;
		table_title_const_height = table_cell_heigth;
	}



	/**
	 * 开始画表格
	 */
	public void startDrawTable(){
		if(table_total_info == null)
			return;
		
		//开始制作表头
		computerTableTitleCoor();
		computerDatasFontWidth();
		drawTableTitle();
		//表头制作完毕
		
		//开始制作表体
		table_main_layout.addView(table_content_lv);
		//初始化adapter
		TableContentAdapter content_adpter = new TableContentAdapter();
		table_content_lv.setAdapter(content_adpter);
	}

	/**
	 * 计算每个单元格的宽度
	 */
	private void computerDatasFontWidth() {
		//表头单元格最大宽度
		List<ArrayList<TableHeadEntity>> list_table_title = table_total_info.tableHead;
		if(list_table_title.isEmpty()){
//			DebugLog.e(tag, "无表头数据");
			Toast.makeText(mContext, "无表头数据", Toast.LENGTH_LONG).show();
			return;
		}
		for(int i=0;i<list_table_title.size();i++){
			ArrayList<TableHeadEntity> cols_title = list_table_title.get(i);	//每一行的title
			
			for(int j=0;j<cols_title.size();j++){
				TableHeadEntity title_cell = cols_title.get(j);
				
				int w = (int) getPixelByText(title_cell.rname);
				
				table_cell_width = (w > table_cell_width) ? w : table_cell_width;
			}
		}
		
		//数据单元格最大宽度
		for (int i = 0; i < table_data.size(); i++) {
			for (int j=0;j<table_data.get(i).length && table_data.get(i)[j] != null && !table_data.get(i)[j].equals("");j++) {
				int w = (int) getPixelByText(table_data.get(i)[j].toString());
				
				table_cell_width = (w > table_cell_width) ? w : table_cell_width;
			}
		}
		
//		DebugLog.e(tag, "每个单元格的宽度:" + table_cell_width);
		if(table_cell_width > table_cell_max) {	//若单元格宽度太大   
			if(table_cell_width >= table_cell_max*2) {
//				table_cell_heigth = table_cell_heigth * (((table_cell_width/table_cell_max)-1)/2 + 1);
				table_cell_heigth = table_cell_heigth * 2;
//				DebugLog.e(tag, "重新定义单元格高度:" + table_cell_heigth);
			}
			table_cell_width = table_cell_max;
		}
		
		//列宽和比屏幕小    列宽重置
		if((table_cell_width * table_title_cols_count < screen_width) && table_title_cols_count > 0){
			table_cell_width = screen_width / table_title_cols_count;
		}
	}
	
	/**
	 * 计算表头坐标
	 *  title_cell.mark_x
	    title_cell.mark_y
	 */
	private void computerTableTitleCoor() {

		List<ArrayList<TableHeadEntity>> list_table_title = table_total_info.tableHead;
		if(list_table_title.isEmpty()){
//			DebugLog.e(tag, "无表头数据");
			Toast.makeText(mContext, "无表头数据", Toast.LENGTH_LONG).show();
			return;
		}
		//table_cols 计算每一行报头的单元格数量
		int[] table_cols = new int[list_table_title.size()];
		for(int i=0;i<list_table_title.size();i++){
			ArrayList<TableHeadEntity> cols_title = list_table_title.get(i);	//每一行的title
			for(int j=0;j<cols_title.size();j++){
				TableHeadEntity title_cell = cols_title.get(j);
				
				int cos_num = Integer.parseInt(title_cell.colspan.trim());
				int row_num = Integer.parseInt(title_cell.rowspan.trim());
				
				for(int k=0;k<row_num;k++){
					if(k+i >= list_table_title.size()){
//						DebugLog.e(tag, "越界:" + title_cell.rname);
						//表头不正常
						continue;
					}
					table_cols[k+i] += cos_num;
				}
			}
		}
		//is_match_table  该表头是否是一个正常的表头		每一行的单元格的数量一致
		boolean is_match_table = true;
		table_title_cols_count = 0;
		for(int i=0;i<table_cols.length;i++){
//			DebugLog.d(tag, "table_cols" + i + "---:" + table_cols[i]);
			if(table_title_cols_count != 0 && table_title_cols_count != table_cols[i]){
				is_match_table = false;
			}
			table_title_cols_count = table_cols[i] > table_title_cols_count?table_cols[i]:table_title_cols_count;
		}
//		DebugLog.d(tag, "is_match_table:" + is_match_table);
		//表头不正常	将没有的表头补起   文字定义为“无数据”
		if(!is_match_table){
			TableHeadEntity empty_cell = new TableHeadEntity();
			empty_cell.colspan = "1";
			empty_cell.rowspan = "1";
			empty_cell.rname = "无数据";
			for(int i=0;i<table_cols.length;i++){
				if(table_cols[i] < table_title_cols_count){
					ArrayList<TableHeadEntity> cols_title = list_table_title.get(i);
					int empty_count = table_title_cols_count - table_cols[i];
//					DebugLog.e(tag, "空单元格个数empty_count: " + empty_count);
					for(int j=0;j<empty_count;j++){
						cols_title.add(empty_cell);
					}
				}
			}
		}
		//mark_table_title_matrix	记录整个表头的矩阵
		int [][] mark_table_title_matrix = new int[list_table_title.size()][table_title_cols_count];
		int mark_x = 0, mark_y = 0;
		int temp_x = 0;
		for(int i=0;i<list_table_title.size();i++){
			ArrayList<TableHeadEntity> cols_title = list_table_title.get(i);	//每一行的title
			//重置记录单元格坐标的x，y值
			mark_x = 0;
			mark_y = i;
//			DebugLog.d(tag, "第" + i + "行表头");
			for(int j=0;j<cols_title.size();j++){
				TableHeadEntity title_cell = cols_title.get(j);					//每个单元格
				
				int cos_num = Integer.parseInt(title_cell.colspan.trim());
				int row_num = Integer.parseInt(title_cell.rowspan.trim());
				
				//当前i行表头，当第k格单元格未被填充,k赋值到mark_x
				for(int k=0;k<mark_table_title_matrix[i].length;k++){
					if(mark_table_title_matrix[i][k]==0){
						temp_x = k;
						mark_x = temp_x;
						break;
					}
				}
				
				//有锁定列fix_cols_num  mark_x相应向前fix_cols_num个单元格
				if(fix_cols_num > 0){
					if(fix_cols_num <= temp_x){
						mark_x = temp_x - fix_cols_num;
					}else{
						title_cell.is_fix_cell = true;
					}
				}
				
				//设置单元格x，y值
				title_cell.mark_x = mark_x;
				title_cell.mark_y = mark_y;
//				DebugLog.d(tag + "_Mark", "第" + i + "行表头记录x值:" + title_cell.mark_x + "======记录y值:" + title_cell.mark_y);
				
				//将单元格的cos_num、row_num以1的形式值赋值到mark_table_title_matrix对应的位置上
				/*	e.g.
				 *	 前提：mark_x = 0;mark_y = 0;
				 *	 cos_num = 2,row_num = 2;
				 *	 结果：mark_table_title_matrix[0][0]=1;mark_table_title_matrix[0][1]=1;
				 *		mark_table_title_matrix[1][0]=1;mark_table_title_matrix[1][1]=1;
				 */
				for(int k=0;k<row_num;k++){
					if(k+i >= list_table_title.size()){
//						DebugLog.e(tag, "越界:" + title_cell.rname);
						//表头不正常
						continue;
					}
					for(int l=temp_x;l<cos_num+temp_x;l++){
						mark_table_title_matrix[k+i][l] = 1;
					}
				}
				
//				DebugLog.d(tag, "title_cell.rname:" + title_cell.rname 
//						+ "==title_cell.colspan:" + title_cell.colspan 
//						+ "==title_cell.rowspan:" + title_cell.rowspan);
			}
		}
	}

	
	/**
	 * 画报表表头
	 */
	private void drawTableTitle(){
		table_main_layout = new LinearLayout(mContext);
		table_main_layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		table_main_layout.setOrientation(LinearLayout.VERTICAL);
		addView(table_main_layout);
		
		table_title_layout = LayoutInflater.from(mContext).inflate(R.layout.layout_table_title_lib, null);
		table_title_layout.setFocusable(true);
		table_title_layout.setClickable(true);
		table_title_layout.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		table_main_layout.addView(table_title_layout
				, new LayoutParams(LayoutParams.WRAP_CONTENT
						, LayoutParams.WRAP_CONTENT));
		
		RelativeLayout fix_layout = (RelativeLayout)table_title_layout.findViewById(R.id.table_title_fix_cols);
		RelativeLayout slide_layout = (RelativeLayout)table_title_layout.findViewById(R.id.table_title_slide_layout);
		table_title_scrollview = 
				(TableView4ScrollView_lib) table_title_layout.findViewById(R.id.table_title_scrollview);
		
		List<ArrayList<TableHeadEntity>> list_table_title = table_total_info.tableHead;
		if(list_table_title.isEmpty()){
//			DebugLog.e(tag, "无表头数据");
			Toast.makeText(mContext, "无表头数据", Toast.LENGTH_LONG).show();
			return;
		}
		for(int i=0;i<list_table_title.size();i++){
			ArrayList<TableHeadEntity> cols_title = list_table_title.get(i);	//每一行的title
			
			for(int j=0;j<cols_title.size();j++){
				TableHeadEntity title_cell = cols_title.get(j);
				
				int cos_num = Integer.parseInt(title_cell.colspan.trim());
				int row_num = Integer.parseInt(title_cell.rowspan.trim());
				
				int cell_width = cos_num * table_cell_width;
				int cell_heigth = row_num * table_title_const_height;
				int cell_x = title_cell.mark_x * table_cell_width;
				int cell_y = title_cell.mark_y * table_title_const_height;
				
				TableView4TitleTextView_lib tv;
				tv = createCTextView(table_title_layout, title_cell.rname);
				tv.setTextColor(titleTextColor);
				tv.setGravity(Gravity.CENTER);
				tv.setText(title_cell.rname);
				tv.setPadding(0, 0, 0, 0);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
				RelativeLayout.LayoutParams fl = new RelativeLayout.LayoutParams(cell_width, cell_heigth);
				fl.leftMargin = cell_x;
				fl.topMargin = cell_y;
				
				if(title_cell.is_fix_cell){
					fix_layout.addView(tv, fl);
				}else{
					slide_layout.addView(tv, fl);
				}
			}
		}
	}
	
	public TableView4TitleTextView_lib createCTextView(View parentLayout,String dataItem){
		TableView4TitleTextView_lib tv;
		if (dataItem != null && !"".equals(dataItem.toString().trim())) {
			tv = new TableView4TitleTextView_lib(parentLayout.getContext(),2);//画右线
		}else {
			tv = new TableView4TitleTextView_lib(parentLayout.getContext(), 3);//不画右线
		}
		return tv;
	}
	
	/**
	 * 计算字符串所占像素
	 * 
	 * @param textSize
	 *            字体大小
	 * @param text
	 *            字符串
	 * @return 字符串所占像素
	 */
	private int getPixelByText(String text) {
		Paint mTextPaint = new Paint();
		mTextPaint.setTextSize(table_font_size); // 指定字体大小
		mTextPaint.setFakeBoldText(true); // 粗体
		mTextPaint.setAntiAlias(true); // 非锯齿效果

		return (int) (mTextPaint.measureText(text) + table_font_size);
	}
	
	/**
	 * 设置报表字体大小
	 * @param table_font_size
	 */
	public void setTable_font_size(float table_font_size) {
		this.table_font_size = table_font_size;
	}

	/**
	 * 
	 * @param fix_cols_num
	 */
	public void setFix_cols_num(int fix_cols_num) {
		this.fix_cols_num = fix_cols_num;
		if(this.fix_cols_num >= 4){
			table_cell_max = screen_width / 8;
			table_cell_heigth *= 1.5;
		}
	}
	
	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}
	
	/**
	 * 
	 * @param zlevel_level		zlevel与level是否相同		(不相同  可下钻)
	 */
	public void setZlevel_level(boolean zlevel_level) {
		this.zlevel_level = zlevel_level;
	}
	
	/**
	 * 
	 * @param qdlevel_level		qdlevel与level是否相同	(相同 是清单 可下钻)
	 */
	public void setQdlevel_level(boolean qdlevel_level) {
		this.qdlevel_level = qdlevel_level;
	}
	
	/**
	 * 设置
	 * @param zlevel_index
	 */
	public void setZlevel_index(int zlevel_index) {
//		DebugLog.d(tag, "可以点击的下钻列数zlevel_index:" + zlevel_index);
		this.zlevel_index = zlevel_index;
	}
	
	public void setQdlevel_index(int qdlevel_index) {
//		DebugLog.d(tag, "可以点击的清单列数qdlevel_index:" + qdlevel_index);
		this.qdlevel_index = qdlevel_index;
	}
	
	public int getCurr_page(){
		return this.curr_page;
	}
	
	public void setIs_paging(boolean paging){
		if(paging){
//			DebugLog.d(tag, "=================================开启分页功能");
			table_content_lv.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

					/*if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
						// 判断是否滚动到底部
						
						if (view.getLastVisiblePosition() == view.getCount() - 1 && isLastRow && callBack != null) {
							DebugLog.d("onScrollStateChanged"
									, "加载数据==================");
							isLastRow = false;
							callBack.callBackResult(999, 999);
						}
					}*/
					if((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE //停止滚动状态
							&& callBack != null) 
							&& isLastRow){
						//加载更多数据
						
						isLastRow = false;
						//回调到act再发出请求获取数据
						callBack.callBackResult(999, 999);
						
//						DebugLog.d("onScrollStateChanged"
//								, "加载数据==================");
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					if(totalItemCount > 0 && (firstVisibleItem + visibleItemCount == totalItemCount)){
//						DebugLog.d("onScrollStateChanged", "已到达底部");
						isLastRow = true;
					}
				}
			});
		}
	}
	
	//================================================================================下面是class或者listener






	/**
	 * 报表表体的adapter
	 * @author Administrator
	 *
	 */
	class TableContentAdapter extends BaseAdapter {
		LayoutInflater mInflater;

		public TableContentAdapter() {
			super();
			mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			if (table_data == null)
				return 0;
			return table_data.size();
		}

		@Override
		public Object getItem(int position) {
			if (table_data == null)
				return null;
			return table_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		private float x, y;
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHoler holer;
			if (convertView == null) {
				holer = new ViewHoler();
				convertView = mInflater.inflate(R.layout.layout_table_title_lib, null);
				holer.fix_layout = (RelativeLayout)convertView.findViewById(R.id.table_title_fix_cols);
				holer.slide_layout = (RelativeLayout)convertView.findViewById(R.id.table_title_slide_layout);
				holer.scrollView1 = (TableView4ScrollView_lib) convertView.findViewById(R.id.table_title_scrollview);
				
				holer.scrollView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
				table_title_scrollview.AddOnScrollChangedListener(new OnScrollChangedListenerImp_Test(holer.scrollView1));
				convertView.setTag(holer);
			}else {
				holer = (ViewHoler)convertView.getTag();
			}
			holer.fix_layout.removeAllViews();
			holer.slide_layout.removeAllViews();
			List<Integer> types0 = new ArrayList<Integer>();
			CharSequence[] dataItem = table_data.get(position);
			CharSequence[] next_dataItem = null;
			if (position < table_data.size()-1) {
				next_dataItem = table_data.get(position+1);
			}

			//改变每行的颜色
			if (typeList != null && typeList.size() > 0) {
				if (typeList.size() > 1) {
					types0 = typeList.get(typeList.size() - 1);
					if (types0 != null && types0.size() > position) {
						dealTypeList(typeList, typeList.size() - 1);
						int temp = depre_color.get(types0.get(position));
						holer.slide_layout.setBackgroundColor(rows_background_color[temp%rows_background_color.length]);
					}
				}
				types0 = typeList.get(0);
				if (types0 != null && types0.size() > position) {
					dealTypeList(typeList, 0);
					int temp = depre_color.get(types0.get(position));
					if (typeList.size() != 1) {
						holer.fix_layout.setBackgroundColor(rows_background_color[temp%rows_background_color.length]);
					}else {
						convertView.setBackgroundColor(rows_background_color[temp%rows_background_color.length]);
					}
				}
			}else{
				convertView.setBackgroundColor(rows_background_color[position% rows_background_color.length]);
			}
			//
			for (int i = 0; i < table_title_cols_count; i++) {
				TableView4TitleTextView_lib tv = null;
				if (dataItem.length <= i){
					tv = new TableView4TitleTextView_lib(convertView.getContext() ,1);//画下线
					tv.setText("无数据");
					tv.setTextColor(contentTextColor);
					tv.setGravity(Gravity.CENTER);
					tv.setPadding(0, 0, 0, 0);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
					RelativeLayout.LayoutParams fl = new RelativeLayout.LayoutParams(table_cell_width, table_cell_heigth);
					if (i < fix_cols_num) {
						fl.leftMargin = i*table_cell_width;
						holer.fix_layout.addView(tv, fl);
					}else {
						fl.leftMargin = (i-fix_cols_num)*table_cell_width;
						holer.slide_layout.addView(tv, fl);
					}
					continue;
				}
				if (typeList != null && typeList.size() > 0) {
					if (i < typeList.size()) {//合并的列数内
						types0 = typeList.get(i);//只有一个合并列
						if (types0.size() > position && position == types0.get(position)) {
							if (next_dataItem != null  && !next_dataItem[i].equals(dataItem[i])) {
								tv = new TableView4TitleTextView_lib(convertView.getContext(), 1);//画下线
							}else if(position == table_data.size()-1){
								tv = new TableView4TitleTextView_lib(convertView.getContext(),1);//画下线
							}else {
								tv = new TableView4TitleTextView_lib(convertView.getContext(),0);//不画下线
							}
							tv.setText(dataItem[i]);
						}else if (next_dataItem != null  && !next_dataItem[i].equals(dataItem[i])) {
							tv = new TableView4TitleTextView_lib(convertView.getContext(), 1);//画下线
							tv.setText("");
						}else if(position == table_data.size()-1){
							tv = new TableView4TitleTextView_lib(convertView.getContext(),1);//画下线
						}else {
							tv = new TableView4TitleTextView_lib(convertView.getContext(),0);//不画下线
							tv.setText("");
						}
					}else {
						if (dataItem != null && dataItem[i] != null && !"".equals(dataItem[i].toString().trim())) {
							tv = new TableView4TitleTextView_lib(convertView.getContext(),1);//画下线
							tv.setText(!TextUtils.isEmpty(dataItem[i])? dataItem[i]:"");
						}else if(position == table_data.size()-1){
							tv = new TableView4TitleTextView_lib(convertView.getContext(),1);//画下线
						}else {
							tv = new TableView4TitleTextView_lib(convertView.getContext(), 0);//不画下线
							tv.setVisibility(View.GONE);
						}
					}
				}else {
					if (dataItem != null && dataItem.length > i && !"".equals(dataItem[i].toString().trim())) {
						tv = new TableView4TitleTextView_lib(convertView.getContext(),1);//画下线
					}else if(position == table_data.size()-1){
						tv = new TableView4TitleTextView_lib(convertView.getContext(),1);//画下线
					}else {
						tv = new TableView4TitleTextView_lib(convertView.getContext(), 0);//不画下线
					}
					tv.setText(dataItem[i]);
				}
				
				tv.setTextColor(contentTextColor);
				tv.setGravity(Gravity.CENTER);
				tv.setPadding(0, 0, 0, 0);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
				if (callBack != null && zlevel_level && !dataItem[i].equals("合计") && !dataItem[i].equals("全省") && i == zlevel_index) {
				    tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
					tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
					        callBack.callBackResult(position);
						}
					});
				}
				if(callBack != null && qdlevel_level && !dataItem[i].equals("合计") && !dataItem[i].equals("全省") && i == qdlevel_index){
			        tv.setTag(position);
			        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			        final String qd_name = tv.getText().toString();
			        tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if("1029".equals(table_total_info.reportId)){
								UNITY_TABLE_QD_NAME = qd_name;
//								DebugLog.d(tag, "qd_name : " + qd_name);
							}
							callBack.callBackResult(position, UNITY_TABLE_QD);
						}
					});
				}
				if (i == 0 && (dataItem[i].equals("全省"))) {
					tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				}
				

				if (("729".equals(report_id)|| "730".equals(report_id)) && i == 1) {
					tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				}else if (("720".equals(report_id) || "721".equals(report_id) 
						|| "722".equals(report_id) || "723".equals(report_id) 
						|| "724".equals(report_id)) && i == 0) {
					tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				}else if (("7221".equals(report_id) || "7231".equals(report_id)
						|| "7241".equals(report_id))
						&& i == 1) {
					tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				}else if ("7254".equals(report_id) && i == 0) {
					tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				}
				
				RelativeLayout.LayoutParams fl = new RelativeLayout.LayoutParams(table_cell_width, table_cell_heigth);
				if (i < fix_cols_num) {
					fl.leftMargin = i*table_cell_width;
					holer.fix_layout.addView(tv, fl);
				}else {
					fl.leftMargin = (i-fix_cols_num)*table_cell_width;
					holer.slide_layout.addView(tv, fl);
				}
				
			}
			return convertView;
		}
		
		class ViewHoler{
			RelativeLayout fix_layout ;
			RelativeLayout slide_layout ;
			TableView4ScrollView_lib scrollView1 ;
		}

		private HashMap<Integer, Integer> depre_color;
		private void dealTypeList(List<ArrayList<Integer>> typeList,int index){
			if (typeList != null && typeList.size() > 0) {
				List<Integer> type0 = typeList.get(index);
				depre_color = new HashMap<Integer, Integer>();
				int temp = 0;
				int compare = 0;
				if (type0 != null && type0.size() == 1) {
					depre_color.put(type0.get(0), temp);
					return;
				}
				if (type0 != null && type0.size() > 1) {
					for (int i = 0; i < type0.size()-1; i++) {
						if (type0.get(i+1) != type0.get(i)) {
							depre_color.put(type0.get(i), temp);
							compare = type0.get(i);
							temp ++;
						}
						
					}
					if (compare != type0.get(type0.size() - 1)) {
						depre_color.put(type0.get(type0.size() - 1), temp);
					}
				}
			}
		}
	}
	
	class OnScrollChangedListenerImp_Test implements OnScrollChangedListener_Test {
		TableView4ScrollView_lib mScrollViewArg;

		public OnScrollChangedListenerImp_Test(TableView4ScrollView_lib scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
		}
	}
	
	/**
	 * 应该是一个联动监听器（联动lietview和表头）
	 * @author Administrator
	 *
	 */
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if(arg0 instanceof TableView4ScrollView_lib){
//				DebugLog.d(tag, "if(arg0 instanceof TableView4ScrollView_Test){");
				table_title_scrollview.setUnmoveScrollview((TableView4ScrollView_lib) arg0);
			}
			table_title_scrollview.onTouchEvent(arg1);
			return false;
		}
	}
	
	/**
	 * Task回调
	 * <一句话功能简述>
	 * <功能详细描述>
	 * 
	 * @author  youjw
	 * @version  [版本号, 2013-11-27]
	 * @see  [相关类/方法]
	 * @since  [产品/模块版本]
	 */
	@SuppressWarnings("hiding")
	public interface TaskCallBack<String> {
		public enum CallBackError{
			RECONNECTION,	//空指针，重连操作
			IOEXCEPTION,	//无法连接服务器
			OTHER_EXCEPTION;		//其他错误
		}
		public void callBackResult(String result);
		public void callBackResult(String result,int code);
		public void callbackError(TaskCallBack.CallBackError error);
	}
}
