package com.gdtel.eshore.androidframework.common.util.imageUpload;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


public class MethodUploadImage {
    public final static String CATEGORY = "android";
    public final static String V = "1.0";
    private static final String TAG = "NetAccessor";

    /**
     * * 联网接口
     * 不需要加密
     * <功能详细描述>
     *
     * @param path   接口的地址（全部地址）
     * @param entity
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String uploadFile(String path, ProgressOutHttpEntity entity) {
        String result = "";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DefaultHttpClient httpClient = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 设置连接超时时间
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpPost httpPost = new HttpPost(path);//创建 HTTP POST 请求
        httpPost.setEntity(entity);


        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(
                    final HttpRequest request,
                    final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                    request.addHeader("Accept-Charset", "gb2312,utf-8");
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(
                    final HttpResponse response,
                    final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    HeaderElement[] codecs = ceheader.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(
                                    new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

        });

        int resultCode = 0;
        try {
            //访问网络
            byte[] bytes = null;
            // 获得HttpResponse实例
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // 判断是否请求成功
            resultCode = httpResponse.getStatusLine().getStatusCode();
            DebugLog.i("文件上传", "网络访问响应码:" + resultCode);
            if (resultCode == HttpStatus.SC_OK) {

                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "文件上传失败:ClientProtocolException";
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            result = "文件上传失败:ConnectTimeoutException";
        } catch (Exception e) {
            e.printStackTrace();
            result = "文件上传失败:Exception";
        } finally {
            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
//		return "网络访问响应码:"+resultCode+"";
        return result;
    }

    static class GzipDecompressingEntity extends HttpEntityWrapper {
        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent()
                throws IOException, IllegalStateException {
            // the wrapped entity's getContent() decides about repeatability  
            InputStream wrappedin = wrappedEntity.getContent();
            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            // length of ungzipped content is not known  
            return -1;
        }

    }
}
