package com.gdtel.eshore.androidframework.ui.view.report.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableInfoRes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3745746451436546738L;

	public String result;//	String	返回代码	必填	0：成功；其他：失败
	public String reason;//	String	错误说明	必填	result=0时返回空
	public String tabTitile;//	String	报表标题	必填	格式如：2014年02月广州移动有效用户持续跟踪月报
	public List<TableDetaileInfo> dataList ;//	String	报表返回的数据集	必填	固定的列名如：col1_code，col2_code(一般不展示)
	public List<ArrayList<TableHeadEntity>> tableHead;//	String	表头	必填	数据会返回表头合并必需参数如：colspan、colspan
	public String sumDate;//	String	时间	必填	格式如：20140302
	public String beginDate;//	String	开始时间	必填	格式如：20140302
	public String areaName;//	String	地区详细名称	必填	例:广州-天河
	public String areaCode;//	String	地区编码	必填	例：-1|440
	public String auditState;//	String	审核状态	必填	格式如(已经审核):S0A 格式如(未审核):S0W 
	public String pfeild;//	String	百分比字段	必填	表格百分比字段，一般用于终端表格特殊处理的字段，如标明不同颜色,例：sum_1,sum_2
	public String zfeild;//	String	钻取字段	必填	表格需要钻取的字段，一般用于终端表格特殊处理的字段，如需要加链接并且可以转到下一级展现数据,
														//例：col1_code,col2_code，数据行中如col1_code=“00”表示该行不用钻
	public String zlevel;//	下转到第几层
	public String pageSizeString;//	String	每页显示的行数	必填	不需要分页，默认值设置：-1 需要分页，默认值设置如：10 、20 、30
	public String curPage;//	String	当前页	必填	如：1
	public String total;//	String	总的记录数	必填	分页总的记录数
	public String reportId;//	String	报表id	必填	如：1017
	public String dateTpye;//	String	String	必填	报表类型
	public String screen;//	String	String	必填	报表展示屏幕
	public String areaTpye;//	String	String	必填	区域树
	public String menuType;//	String	String	必填	运营数据(默认)：1 , 划小数据：2
	public String level;//区域层级
	public String qdfeild;//单字段标识，标明哪一列有清单
	public String qdlevel;//清单从哪一级开始有
	public String unit;	//报表单位名称，如：单位：户
	public String type;    //第一次可以不填 默认为0 第二次传第一次请求返回type值 转售商日报
	public String isQd;	//是否是清单
	
	public static class TableDetaileInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 72852912513538023L;
		public String corp_id;
		public String corp_name;
		public String col1_code;
		public String col1_name;
		public String col2_code;
		public String col2_name;
		public String col3_code;
		public String col3_name;
		public String col4_code;
		public String col4_name;
		public String col5_code;
		public String col5_name;
		public String col6_code;
        public String col6_name;
        public String col7_code;
        public String col7_name;
        public String col8_code;
        public String col8_name;
        public String col9_code;
        public String col9_name;
        public String col10_code;
        public String col10_name;
        public String col11_code;
        public String col11_name;
        public String col12_code;
        public String col12_name;
        public String col13_code;
        public String col13_name;
        public String col14_code;
        public String col14_name;
        public String col15_code;
        public String col15_name;
		public String area_name;
		public String row1_code;
		public String row2_code;
		public String row3_code;
		public String sums_1;
		public String sums_2;
		public String sums_3;
		public String sums_4;
		public String sums_5;
		public String sums_6;
		public String sums_7;
		public String sums_8;
		public String sums_9;
		public String sums_10;
		public String sums_11;
		public String sums_12;
		public String sums_13;
		public String sums_14;
		public String sums_15;
		public String sums_16;
		public String sums_17;
		public String sums_18;
		public String sums_19;
		public String sums_20;
		public String sums_21;
		public String sums_22;
		public String sums_23;
		public String sums_24;
		public String sums_25;
		public String sums_26;
		public String sums_27;
		public String sums_28;
		public String sums_29;
		public String sums_30;
		public String sums_31;
        public String sums_32;
        public String sums_33;
        public String sums_34;
        public String sums_35;
        public String sums_36;
        public String sums_37;
        public String sums_38;
        public String sums_39;
        public String sums_40;
        public String sums_41;
        public String sums_42;
        public String sums_43;
        public String sums_44;
        public String sums_45;
        public String sums_46;
        public String sums_47;
        public String sums_48;
        public String sums_49;
        public String sums_50;
        public String sums_51;
        public String sums_52;
        public String sums_53;
        public String sums_54;
        public String sums_55;
        public String sums_56;
        public String sums_57;
        public String sums_58;
        public String sums_59;
        public String sums_60;

        //平台自定义属性
        public static class PlatformSelfParams implements Serializable{
            private static final long serialVersionUID = 1L;
            
            public String kj_where;
            public String var_name;
            public String kj_type;
            public String user_type;
            public String v_type;
            public String user_account_v;
            public String is_system;
            public String kj_title;
            public String org_1_v;
            public String org_2_v;
            public String org_3_v;
            public String org_4_v;
            public String org_5_v;
            public String org_6_v;
            public String sys_var;
            public String not_empty;
            public String mr_value;
            public String pList;//应该是一个list
            public String report_config_id;
        }
	}
}
