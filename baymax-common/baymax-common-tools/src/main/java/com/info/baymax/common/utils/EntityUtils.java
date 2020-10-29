package com.info.baymax.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class EntityUtils {

	public static <T> T getDefaultIfNull(T src, T def) {
		return null == src ? def : src;
	}

	// Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
	public static void transMap2Bean(Map<String, Object> map, Object obj) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			String key = null;
			Object value = null;
			Method setter = null;
			for (PropertyDescriptor property : propertyDescriptors) {
				key = property.getName();
				if (map.containsKey(key)) {
					value = map.get(key);
					// 得到property对应的setter方法
					setter = property.getWriteMethod();
					setter.invoke(obj, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	public static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					if (getter != null) {
						Object value = getter.invoke(obj);
						map.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) throws Exception {
		if (map == null)
			return null;
		T obj = beanClass.newInstance();
		org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		return obj;
	}

	public static Map<?, ?> beanToMap(Object obj) {
		if (obj == null)
			return null;
		return new org.apache.commons.beanutils.BeanMap(obj);
	}

	public static Object map2Bean(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null)
			return null;

		Object obj = beanClass.newInstance();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}
		return obj;
	}

	public static Map<String, Object> bean2Map(Object obj) throws Exception {
		if (obj == null)
			return null;
		Map<String, Object> reMap = new HashMap<String, Object>();
		Class<?> objClass = obj.getClass();
		while (objClass != null) {
			Field[] fields = objClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					Field f = objClass.getDeclaredField(fields[i].getName());
					f.setAccessible(true);
					Object o = f.get(obj);
					reMap.put(fields[i].getName(), o);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			objClass = objClass.getSuperclass();
		}
		return reMap;
	}

	/*
	 * public static void main(String[] args) { Integer defaultIfNull =
	 * getDefaultIfNull(null, 1); System.out.println(defaultIfNull.getClass());
	 * Long defaultIfNull2 = getDefaultIfNull(null, 1L);
	 * System.out.println(defaultIfNull2.getClass()); Double defaultIfNull3 =
	 * getDefaultIfNull(null, 1D);
	 * System.out.println(defaultIfNull3.getClass()); SysUser defaultIfNull4 =
	 * getDefaultIfNull(null, new SysUser());
	 * System.out.println(defaultIfNull4.getClass()); String defaultIfNull5 =
	 * getDefaultIfNull(null, "");
	 * System.out.println(defaultIfNull5.getClass()); }
	 */
}
