package com.gdtel.eshore.androidframework.common.entity;

import java.io.Serializable;

import org.json.JSONObject;

import android.text.TextUtils;

import com.gdtel.eshore.androidframework.common.util.AppConstant;


/**
 * 接口返回公共结果
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2014-2-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BaseResult implements Serializable{
	
	private static final long serialVersionUID = -6424116952458101106L;
	
	public String isSuccesss;
	public String reason;
	
	public static boolean isResultOK(BaseResult result){
		if(result != null && !TextUtils.isEmpty(result.isSuccesss) 
				&& result.isSuccesss.equals(AppConstant.RESULT_OK)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isMsgUnEmity(BaseResult result){
		if(result != null && !TextUtils.isEmpty(result.reason)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 解析公共返回信息
	 * @param baseResult
	 * @param jsonObject
	 * @return
	 */
	public static boolean parseBaseResult(BaseResult baseResult, JSONObject jsonObject) {
		baseResult.isSuccesss = jsonObject.optString("isSuccesss");
		baseResult.reason = jsonObject.optString("reason");
		return AppConstant.RESULT_OK.equals(baseResult.isSuccesss);
	}
}
