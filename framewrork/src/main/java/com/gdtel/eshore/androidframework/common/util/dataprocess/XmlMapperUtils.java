package com.gdtel.eshore.androidframework.common.util.dataprocess;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
/**
 * xml 解析类
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author  youjw
 * @version  [版本号, 2013-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class XmlMapperUtils {

	private static final String TAG = XmlMapperUtils.class.getSimpleName();

	private static Serializer serializer;

	private static byte[] lock = new byte[0];

	public static Serializer getDefaultObjectMapper() {
		synchronized (lock) {
			if (serializer == null) {
				serializer = new Persister(new Format(
						"<?xml version=\"1.0\" encoding=\"utf-8\"?>"));
			}
		}
		return serializer;
	}

	/**
	 * 对象转换成 xml
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T> String toXml(T t) {
		if (t == null)
			return null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			getDefaultObjectMapper().write(t, out);
			return out.toString();
		} catch (Exception e) {
			DebugLog.w(TAG, e);
			return null;
		}

	}

	/**
	 * xml 转换成对象
	 * @param <T>
	 * @param xml
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String xml, Class<T> clazz) {
		try {
			return getDefaultObjectMapper().read(clazz, xml);
		} catch (Exception e) {
			DebugLog.w(TAG, e);
			return null;
		}
	}

	/**
	 * xml二进制流转成对象
	 * @param <T>
	 * @param in
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(InputStream in, Class<T> clazz) {
		try {
			return getDefaultObjectMapper().read(clazz, in);
		} catch (Exception e) {
			DebugLog.w(TAG, e);
			return null;
		}
	}

}
