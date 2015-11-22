/**
 * 
 */
package com.gdtel.eshore.androidframework.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.TextUtils;

/**
 * @author Smart_Chow
 * 2010-1-26 上午08:23:34
 *
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {
	/**
	 * 将yyyyMMddHHmmss格式转换成yyyy-MM-dd 例如2010012518244000转成2010-1-25
	 * @param str
	 * @return
	 * @throws ParseException
	 */
		public static String format(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date =  format.parse(str);
			format.applyPattern("yyyy-MM-dd");
			return format.format(date);
		}
		
		/**
		 * 将yyyyMMdd格式转换成yyyy-MM-dd 
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String format1(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date =  format.parse(str);
			format.applyPattern("yyyy-MM-dd");
			return format.format(date);
		}
			
		/**
		 * 将yyyy-MM-dd 01-25
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatMD(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date =  format.parse(str);
			format.applyPattern("MM-dd");
			return format.format(date);
		}
		
		/**
		 * 12月12日 --> 12-12
		 * @param str
		 * @return
		 */
		public static String formatMD1(String str) {
			String patternDate = "^([1-9]|10|11|12)月([1-2][0-9]|[1-9]|30|31)日$";
			if (TextUtils.isEmpty(str) || !Pattern.compile(patternDate).matcher(str).matches()) {
				return "";
			}
			String month = str.substring(0,str.indexOf("月"));
			String day = str.substring(str.indexOf("月")+1,str.indexOf("日"));
			return month +"-"+ day;
		}
		
		/**
		 * 将yyyy-MM-dd 01月25日
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatMD_CN(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date =  format.parse(str);
			format.applyPattern("MM月dd日");
			return format.format(date);
		}
		
		/**
		 * 将MM-dd 01月25日
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatMD_CN2(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("MM-dd");
			Date date =  format.parse(str);
			format.applyPattern("MM月dd日");
			return format.format(date);
		}
		
		/**
		 * 将yyyyMMdd 01月25日
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatMD_CN1(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date =  format.parse(str);
			format.applyPattern("MM月dd日");
			return format.format(date);
		}
		
		/**
		 * 将yyyyMMdd格式转换成yyyy年MM月dd日 
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatYMD_CN(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date =  format.parse(str);
			format.applyPattern("yyyy年MM月dd日");
			return format.format(date);
		}
		
		/**
		 * 将yyyy-MM-dd格式转换成yyyy年MM月dd日 
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatYMD_CN1(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date =  format.parse(str);
			format.applyPattern("yyyy年MM月dd日");
			return format.format(date);
		}
		/**
		 * 将yyyyMM格式转化为yyyy年MM月
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatMD_CN_new(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			Date date =  format.parse(str);
			format.applyPattern("yyyy年MM月");
			return format.format(date);
		}
		/**
		 * 将yyyyMMdd格式转化为yyyy年MM月dd日
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static String formatMD_CN_new_1(String str) throws ParseException{
			if(str==null||"".equals(str)){
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date =  format.parse(str);
			format.applyPattern("yyyy年MM月dd日");
			return format.format(date);
		}
		
		/**
		 * 将输入的字符串按照指定的格式转化为日期
		 * @param str
		 * @param pattern
		 * @return
		 * @throws ParseException
		 */
		public static Date format(String str,String pattern) throws ParseException{
			if(str==null||"".equals(str)){
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return  format.parse(str);
		}
		
		/**
		 * 时间格式转化，如：转换20101101为201011
		 * @param str
		 * @param pattern
		 * @return
		 * @throws ParseException
		 */
		public static String formatStr(String str,String pattern) throws ParseException{
			if(str==null||"".equals(str)){
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date = format.parse(str);
			format.applyPattern(pattern);
			return  format.format(date);
		}
		
		/**
		 * 判断当前日期是否在指定的两个日期内
		 * @param begin
		 * @param end
		 * @return
		 */
		public static boolean isBetween(String begin,String end){
			try {
				Date beginDate = format(begin,"yyyyMMddHHmmss");
				Date endDate = format(end,"yyyyMMddHHmmss");
				Date currentDate = new Date();
				if(beginDate!=null&&endDate!=null){
					if(beginDate.getTime()<=currentDate.getTime()&&endDate.getTime()>=currentDate.getTime()){
						return true;
					}
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		/**
		 * 将时间字符串转化为日期，如：20131118143758转化为2013-11-18 14:37:58
		 * @param dateString
		 * @return
		 */
		public static String getDateTime(String dateString) {
			String datetime = "";
			if (!TextUtils.isEmpty(dateString) && dateString.length() >= 14) {
				datetime = dateString.substring(0, 4) + "-"
						+ dateString.substring(4, 6) + "-"
						+ dateString.substring(6, 8) + " "
						+ dateString.substring(8, 10) + ":"
						+ dateString.substring(10, 12) + ":"
						+ dateString.substring(12, 14);
			}
			return datetime;
		}
		
		/**
		 * 从时间字符串中获取年月日
		 * @param dateString
		 * @return
		 */
		public static String getDate(String dateString) {
			String date = "";
			if (!TextUtils.isEmpty(dateString) && dateString.length() >= 8) {
				date = dateString.substring(0, 4) + "-"
						+ dateString.substring(4, 6) + "-"
						+ dateString.substring(6, 8);
			}
			return date;
		}
		
		/**
		 * 获取年份
		 * @return
		 */
		public static String getCurYear() {
			DateFormat mDateFormat  = new SimpleDateFormat("yyyy");
			Calendar cl_date = Calendar.getInstance();
			
			return mDateFormat.format(cl_date.getTime());
		} 
		
		/**
		 * 获取本日日期str
		 * @return
		 */
		public static String getCurDate() {
			DateFormat mDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cl_date = Calendar.getInstance();
			
			return mDateFormat.format(cl_date.getTime());
		}
		
		/**
		 * 获取前一天日期
		 * @return
		 */
		public static String getPreviousDate() {
			DateFormat mDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cl_date = Calendar.getInstance();
			cl_date.add(Calendar.DAY_OF_MONTH, -1);
			
			return mDateFormat.format(cl_date.getTime());
		}
		
		/**
		 * 获取本日详细时间
		 * @return
		 */
		public static String getCurDateHHMMSS() {
			DateFormat mDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cl_date = Calendar.getInstance();
			
			return mDateFormat.format(cl_date.getTime());
		}
		
		/**
		 * 获取当前时间
		 * @return
		 */
		public static String getCurTimemmss() {
			DateFormat mDateFormat  = new SimpleDateFormat("HH:mm");
			Calendar cl_date = Calendar.getInstance();
			
			return mDateFormat.format(cl_date.getTime());
		}
		
		/***
		 * 获取一周之前的日期
		 * @return
		 */
		public static String getLastWeekDate() {
			DateFormat mDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar cl_date = Calendar.getInstance();
			
			long time = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
			Date date = new Date(time);
			return mDateFormat.format(date);
		}
		
		/**
		 * 创建系统日期选择
		 * @param context
		 * @param tv
		 * @return
		 */
		public static DatePickerDialog createDateDialog(Context context,OnDateSetListener onDateSetListener){
			Calendar c = Calendar.getInstance(); 
			DatePickerDialog dateDialog = new DatePickerDialog(
					context,onDateSetListener,
					c.get(Calendar.YEAR),
					c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			
			return dateDialog;
		}
		
		
		
}
