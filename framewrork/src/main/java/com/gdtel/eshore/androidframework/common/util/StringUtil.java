package com.gdtel.eshore.androidframework.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Paint;
import android.text.TextUtils;

public class StringUtil {

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean isMatch(String regex, String orginal){
		if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	/**
	 * 正整数
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	/**
	 * 负整数
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	/**
	 * 整数
	 * @param orginal
	 * @return
	 */
	public static boolean isWholeNumber(String orginal) {
		return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}
	
	/**
	 * 正小数
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveDecimal(String orginal){
		return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
	}
	
	/**
	 * 负小数
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeDecimal(String orginal){
		return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
	}
	
	/**
	 * 小数
	 * @param orginal
	 * @return
	 */
	public static boolean isDecimal(String orginal){
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}
	
	/**
	 * 实数
	 * @param orginal
	 * @return
	 */
	public static boolean isRealNumber(String orginal){
		return isWholeNumber(orginal) || isDecimal(orginal);
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
	public static int GetPixelByText(float textSize, String text) {
		Paint mTextPaint = new Paint();
		mTextPaint.setTextSize(textSize); // 指定字体大小
		mTextPaint.setFakeBoldText(true); // 粗体
		mTextPaint.setAntiAlias(true); // 非锯齿效果

		return (int) (mTextPaint.measureText(text) + textSize);
	}

	public static String isStringEmpty(Object text){
		if (text == null) {
			return "";
		}
		if (text.equals(0)) {
			return "";
		}
		return !TextUtils.isEmpty((String)text)?(String)text:"";
	}
}
