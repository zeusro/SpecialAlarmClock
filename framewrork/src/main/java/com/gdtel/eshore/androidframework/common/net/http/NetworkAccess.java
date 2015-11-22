package com.gdtel.eshore.androidframework.common.net.http;

import android.text.TextUtils;

import com.gdtel.eshore.androidframework.common.util.dataprocess.JsonMapperUtils;
import com.gdtel.eshore.androidframework.common.util.dataprocess.XmlMapperUtils;
import com.gdtel.eshore.androidframework.common.util.log.DebugLog;
import com.gdtel.eshore.androidframework.common.util.security.DesUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;


/**
 * 网络访问类 <一句话功能简述> <功能详细描述>
 *
 * @author youjw
 * @version [版本号, 2013-11-20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class NetworkAccess {
    private static final String TAG = NetworkAccess.class.getSimpleName();
    private static final int REQUEST_TIMEOUT = 20 * 1000;// 设置请求超时20秒钟
    private static final int SO_TIMEOUT = 20 * 1000; // 设置等待数据超时时间20秒钟

    public static enum ContentType {
        JSON, XML
    }

    ;

    public static enum MethodType {
        GET, POST
    }

    private HttpComponentsClientHttpRequestFactory getHttpClient() {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(REQUEST_TIMEOUT);
        //Set the socket read timeout for the underlying HttpClient. A timeout value of 0 specifies an infinite timeout.
        requestFactory.setReadTimeout(SO_TIMEOUT);
        return requestFactory;
    }


    /**
     * post请求
     *
     * @param path
     * @param params
     * @return
     * @throws RestClientException
     */
    public byte[] getStream(String path, Map<String, String> params) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.setRequestFactory(getHttpClient());
        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(path, mapToMultiMap(params), byte[].class);
        byte[] bytes = null;
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            bytes = responseEntity.getBody();
        }

        return bytes;
    }


    /**
     * 将paramJson加密后写入请求实体中
     *
     * @param path
     * @param paramJson
     * @return
     * @throws IOException
     * @throws RestClientException
     */
    public byte[] getStream(String path, String paramJson, int keyIndex) throws IOException {

//		 paramJson = "{\"Mobile\":\"19011701109\",\"Pwd\":\"123123\",\"Way\":\"P\",\"Url\":null,\"IsRemember\":true}";
        DebugLog.i("即将加密的明文未转换", paramJson);
        // 加密后的字节数组
        byte[] bytes = DesUtils.encrypt(paramJson, keyIndex);
        String param = DesUtils.toHexString(bytes);
        DebugLog.i("加密后的字符串", param);
        // Base64编码
        String base64Json = android.util.Base64.encodeToString(bytes,
                android.util.Base64.DEFAULT);
        // UTF16-8编码
        // String encodeJson = URLEncoder.encode(base64Json,"utf-8");
        DebugLog.i("加密后加bytes编码即将上传的字符串", base64Json);

        return getStream(path, base64Json);
    }

    /**
     * 将paramJson不加密写入请求实体中
     *
     * @param path
     * @param paramJson
     * @return
     * @throws IOException
     * @throws RestClientException
     */
    public byte[] getStream(String path, String paramJson) throws IOException {
        DebugLog.i("请求地址", path);
        DebugLog.i("请求的字符串", paramJson);
        byte[] entity = paramJson.getBytes();
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(REQUEST_TIMEOUT);
        conn.setReadTimeout(SO_TIMEOUT);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // POST请求要设置这个属性
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
//		 content-type:application/octet-stream
        conn.setRequestProperty("Content-Length", entity.length + "");
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");    //设置头参数
        OutputStream out = conn.getOutputStream();
        // 将参数值写入
        out.write(entity);
        out.flush();
        out.close();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        DebugLog.i("请求响应码", conn.getResponseCode() + "");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            DebugLog.i("请求响应码", conn.getContentEncoding());
            InputStream input = conn.getInputStream();
            if (input == null) {
                return null;
            } else {
                String encoding = conn.getContentEncoding();
                if (encoding != null && encoding.contains("gzip")) {//首先判断服务器返回的数据是否支持gzip压缩，
                    input = new GZIPInputStream(input);    //如果支持则应该使用GZIPInputStream解压，否则会出现乱码无效数据
                }
                while ((len = input.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.flush();
                outStream.close();
                input.close();
            }
        } else {
            InputStream inputStream = conn.getErrorStream();
            if (inputStream == null) {
                return null;
            } else {
                String encoding = conn.getContentEncoding();
                if (encoding != null && encoding.contains("gzip")) {//首先判断服务器返回的数据是否支持gzip压缩，
                    inputStream = new GZIPInputStream(inputStream);    //如果支持则应该使用GZIPInputStream解压，否则会出现乱码无效数据
                }
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.flush();
                outStream.close();
                inputStream.close();
            }
        }
        if (conn != null)
            conn.disconnect();

        return outStream.toByteArray();
    }

    /**
     * get请求
     *
     * @param path
     * @return
     * @throws RestClientException
     */
    public byte[] getStream(String path) throws RestClientException {
        byte[] bytes = null;
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.setRequestFactory(getHttpClient());

        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(path,
                byte[].class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            bytes = responseEntity.getBody();
        }
        return bytes;
    }


    /**
     * 返回具体的结果
     *
     * @param <T>
     * @param path
     * @param params
     * @param clazz
     * @param contentType
     * @return
     * @throws RestClientException
     */
    public <T> T getEntityByStream(String path, Map<String, String> params,
                                   Class<T> clazz, ContentType contentType, MethodType methodType)
            throws Exception {
        T t = null;
        byte[] bytes = null;
        if (methodType == MethodType.POST) {
            bytes = getStream(path, params);
        } else if (methodType == MethodType.GET) {
            bytes = getStream(getUrl(params, path));
        }
        if (bytes != null && bytes.length > 0) {
            if (contentType == ContentType.JSON) {
                // 需要解密
                // String json = Des3Util.decryptMode(new String(bytes,
                // "UTF-8"));
                String json = new String(bytes, "UTF-8");
                DebugLog.i(TAG, "Result:-->" + json);
                t = JsonMapperUtils.toObject(json, clazz);
            } else if (contentType == ContentType.XML) {
                String xml = new String(bytes, "UTF-8");
                DebugLog.i(TAG, "Result:-->" + xml);
                t = XmlMapperUtils.toObject(xml, clazz);
            }
        }
        return t;
    }

    /**
     * 将参数json实例化之后传入
     *
     * @param path
     * @param paramJson
     * @param contentType
     * @param methodType
     * @param keyIndex
     * @return
     * @throws IOException
     */
    public String getJsonByStream(String path, String paramJson,
                                  ContentType contentType, MethodType methodType, int keyIndex) throws IOException {
        byte[] bytes = null;
        String json = "";
        if (methodType == MethodType.POST) {
            bytes = getStream(path, paramJson, keyIndex);
        } else if (methodType == methodType.GET) {
            bytes = getStream(path);
        }
        if (bytes != null && bytes.length > 0) {
            json = new String(bytes, "UTF-8");
            DebugLog.i(TAG, "Result:-->" + json);
            // t = JsonMapperUtils.toObject(json, clazz);
        }
        return json;
    }


    /**
     * 将参数json实例化之后传入
     * 不加密
     *
     * @param path
     * @param paramJson
     * @param contentType
     * @param methodType
     * @return
     * @throws IOException
     */
    public String getJsonByStream(String path, String paramJson,
                                  ContentType contentType, MethodType methodType) throws IOException {
        byte[] bytes = null;
        String json = "";
        if (methodType == MethodType.POST) {
            bytes = getStream(path, paramJson);
        } else if (methodType == MethodType.GET) {
            bytes = getStream(path);
        }
        if (bytes != null && bytes.length > 0) {
            json = new String(bytes, "UTF-8");
            DebugLog.i(TAG, "Result:-->" + json);
            // t = JsonMapperUtils.toObject(json, clazz);
        }
        return json;
    }

    /**
     * 返回报文
     *
     * @param path
     * @param params
     * @param contentType
     * @param methodType
     * @return
     * @throws Exception
     */
    public String getJsonByStream(String path, Map<String, String> params, ContentType contentType, MethodType methodType) throws Exception {
        String result = "";
        byte[] bytes = null;
        if (methodType == MethodType.POST) {
            bytes = getStream(path, params);
        } else if (methodType == MethodType.GET) {
            bytes = getStream(getUrl(params, path));
        }
        if (bytes != null && bytes.length > 0) {
            if (contentType == ContentType.JSON) {
                // 需要解密
                // String json = Des3Util.decryptMode(new String(bytes,
                // "UTF-8"));
                result = new String(bytes, "UTF-8");
                DebugLog.i(TAG, "Result:-->" + result);
            } else if (contentType == ContentType.XML) {
                result = new String(bytes, "UTF-8");
                DebugLog.i(TAG, "Result:-->" + result);
            }
        }
        return result;
    }

    public <T> T getEntityByStream(String json, Class<T> clazz) {
        T t = null;
        try {
            t = JsonMapperUtils.toObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * @param <T>
     * @param path
     * @param params
     * @param clazz
     * @return
     * @throws RestClientException
     */
    public <T> T getEntity(String path, Map<String, String> params,
                           Class<T> clazz) throws RestClientException {
        T t = null;
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate
                .setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        t = restTemplate.postForObject(path, mapToMultiMap(params), clazz);
        return t;
    }

    private MultiValueMap<String, String> mapToMultiMap(
            Map<String, String> params) {
        MultiValueMap<String, String> paramMultiMap = new LinkedMultiValueMap<String, String>();
        if (params == null || params.size() == 0)
            return paramMultiMap;
        for (String key : params.keySet()) {
            paramMultiMap.add(key, params.get(key));
        }
        return paramMultiMap;
    }

    /**
     * get请求地址
     *
     * @param params
     */
    public String getUrl(Map<String, String> params, String url) {
        if (TextUtils.isEmpty(url))
            return null;
        StringBuilder sb = null;
        try {
            if (url.contains("?")) {
                sb = new StringBuilder(url);
            } else {
                sb = new StringBuilder(url + "?");
            }
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
            DebugLog.e(TAG, " " + e.getMessage());
            e.printStackTrace();
        }
        return sb.toString();
    }


}
