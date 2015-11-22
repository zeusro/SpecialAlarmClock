package com.gdtel.eshore.androidframework.ui.view.report.entity;

import java.io.Serializable;

public class TableHeadEntity implements Serializable{

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = -6420265903399164707L;
	public String colspan;
	public String rname;
	public String rowspan;
	
	//自定义表头位置坐标
	public int mark_x;						//坐标x
	public int mark_y;						//坐标y
	public boolean is_fix_cell = false;		//自定义属性        是否滚动表头
	public String getColspan() {
		return colspan;
	}
	public void setColspan(String colspan) {
		this.colspan = colspan;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getRowspan() {
		return rowspan;
	}
	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}
	
}
