package com.gdtel.eshore.androidframework.common.util.dataprocess;

import java.io.InputStream;
import java.util.ArrayList;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import com.gdtel.eshore.androidframework.common.util.log.DebugLog;

/**
 * json解析
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2013-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JsonMapperUtils {

	private static final String TAG = JsonMapperUtils.class.getSimpleName();

	private static ObjectMapper mMapper;

	private static byte[] lock = new byte[0];

	public static ObjectMapper getDefaultObjectMapper() {
		synchronized (lock) {
			if (mMapper == null) {
				ObjectMapper mapper = new ObjectMapper();
				SerializationConfig seriConf = mapper.getSerializationConfig();
				seriConf.setSerializationInclusion(Inclusion.NON_NULL);
				mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false); //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
				mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,
								false);
				mapper.configure(Feature.AUTO_CLOSE_SOURCE, false);
				// mapper.registerSubtypes(//
				// User.class
				// );
				mMapper = mapper;
			}
		}
		return mMapper;
	}

	/**
	 * 对象转成json
	 * @param <T>
	 * @param t
	 * @return
	 */
	/**
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param t
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static <T> String toJson(T t) {
		if (t == null)
			return null;
		try {
			return getDefaultObjectMapper().writeValueAsString(t);
		} catch (Exception e) {
			DebugLog.w(TAG, e);
			return null;
		}

	}

	/**
	 * json 转成 对象
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return getDefaultObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
			DebugLog.w(TAG, e);
			return null;
		}
	}
	
	/**
	 * json二进制流转成对象
	 * @param <T>
	 * @param in
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(InputStream in, Class<T> clazz) {
		try {
			return getDefaultObjectMapper().readValue(in, clazz);
		} catch (Exception e) {
			DebugLog.w(TAG, e);
			return null;
		}
	}

	public static <T> JavaType constructListType(Class<T> clazz) {
		return TypeFactory.defaultInstance().constructCollectionType(
				ArrayList.class, clazz);
	}
}
