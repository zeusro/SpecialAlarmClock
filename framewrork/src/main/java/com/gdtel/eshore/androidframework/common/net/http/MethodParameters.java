package com.gdtel.eshore.androidframework.common.net.http;

import android.content.Context;

import com.gdtel.eshore.androidframework.common.entity.LoginResult;
import com.gdtel.eshore.androidframework.common.util.AppConstant;
import com.gdtel.eshore.androidframework.common.util.log.DebugLog;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络访问 <一句话功能简述> <功能详细描述>
 *
 * @author youjw
 * @version [版本号, 2013-11-20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class MethodParameters {

    public final static String CATEGORY = "android";
    public final static String V = "1.0";
    private static final String TAG = "NetAccessor";

    /**
     * 系统参数
     *
     * @param paramsMap
     */
    public static void initSysParams(Context ctx,
                                     HashMap<String, String> paramsMap) {
    }

    /**
     * 打印测试链接
     *
     * @param params
     */
    public static void testUrl(HashMap<String, String> params, String url) {
        try {
            // 测试代码
            StringBuilder sb = new StringBuilder(url + "?");
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(entry.getKey())
                            .append('=')
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                            .append('&');
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            DebugLog.i(TAG, "URL：" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录接口
     * <功能详细描述>
     *
     * @param ctx
     * @param name
     * @param password
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static LoginResult login(Context ctx, String name,
                                    String password) {
        LoginResult result = null;
        HashMap<String, String> params = new HashMap<String, String>();
        initSysParams(ctx, params);
        params.put("cmd", "Login");
        params.put("name", "" + name);
        params.put("password", "" + password);
        testUrl(params, AppConstant.BASE_URL);
        try {
            result = new NetworkAccess().getEntityByStream(AppConstant.BASE_URL, params, LoginResult.class, NetworkAccess.ContentType.JSON, NetworkAccess.MethodType.POST);
        } catch (Exception e) {
            DebugLog.d(TAG, "" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
