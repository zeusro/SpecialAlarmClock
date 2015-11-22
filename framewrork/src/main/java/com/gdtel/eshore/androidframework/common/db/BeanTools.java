package com.gdtel.eshore.androidframework.common.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.apache.commons.beanutils.ConvertUtils;

import android.text.TextUtils;

/**
 * 
 * <一句话功能简述>Bean工具类
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2013-10-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BeanTools {
	/**
	 * 获取第一个泛型类
	 */
	public static Class<?> getGenericClass(Class<?> clazz) {
		return getGenericClass(clazz, 0);
	}

	/**
	 * 获取泛型类
	 */
	public static Class<?> getGenericClass(Class<?> clazz, int index) throws IndexOutOfBoundsException {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size of Parameterized Type: " + params.length);
		}
		return (Class<?>) params[index];
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			//logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 强行设置Field可访问.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	protected static Field getDeclaredField(final Object object, final String fieldName) {
		//Assert.notNull(object, "object不能为空");
		//Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value 待转换的字符串
	 * @param toType 转换目标类型
	 */
	public static Object convertStringToObject(String value, Class<?> toType) {
		if (!TextUtils.isEmpty(value)) {
			return ConvertUtils.convert(value, toType);
		} else {
			return null;
		}
	}

	/**
	 * 强行获取私有属性的值
	 */
	public static Object getPrivateProperty(Object object, String propertyName) throws IllegalAccessException, NoSuchFieldException {
		//Assert.notNull(object);
		//Assert.hasText(propertyName);
		Field field = object.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		return field.get(object);
	}

	/**
	 * 强行设置私有属性的值
	 */
	public static void setPrivateProperty(Object object, String propertyName, Object newValue) throws IllegalAccessException, NoSuchFieldException {
		//Assert.notNull(object);
		//Assert.hasText(propertyName);
		Field field = object.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		field.set(object, newValue);
	}
	
	/**
	 * 获取所有字段
	 * @param entityClass 实体的类型
	 * @return data 
	 * 			返回包含两个数组的HashMap，可参考以下使用方法：
	 * 			String[] fieldName = (String[]) data.get("fieldName");
	 * 			Class<?>[] fieldType = (Class<?>[]) data.get("fieldType");
	 */
	public static HashMap<Object, Object> getAllFiled(Class<?> entityClass){
		HashMap<Object, Object> data = new HashMap<Object, Object>();
		
		Field[]  fields = entityClass.getDeclaredFields();
		String[] fieldName = new String[fields.length];
		Class<?>[] fieldType = new Class<?>[fields.length];
		
		for(int i=0; i<fields.length; i++){
			fieldName[i] = fields[i].getName();//组装名称数组
			fieldType[i] = fields[i].getType();//组装类型数组
		}
		
		data.put("fieldName", fieldName);
		data.put("fieldType", fieldType);
		
		return data;
	}
}
