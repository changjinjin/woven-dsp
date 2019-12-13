package com.info.baymax.common.mybatis.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
	/**
	 * 利用反射获取指定对象的指定属性
	 *
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @return 目标属性的值
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = ReflectUtils.getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 利用反射获取指定对象里面的指定属性
	 *
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @return 目标字段
	 */
	private static Field getField(Object obj, String fieldName) {
		Field field = null;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
			}
		}
		return field;
	}

	/**
	 * 利用反射设置指定对象的指定属性为指定的值
	 *
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @param fieldValue
	 *            目标值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void setFieldValue(Object obj, String fieldName, String fieldValue)
			throws IllegalArgumentException, IllegalAccessException {
		Field field = ReflectUtils.getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			field.set(obj, fieldValue);
		}
	}

	/**
	 * 根据文件路径 获取反射对象并执行对应方法
	 *
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Object reflectByPath(String path)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		// 获取类名
		String className = path.substring(0, path.lastIndexOf("."));
		// 获取方法名
		String methodName = path.substring(path.lastIndexOf(".") + 1, path.length());
		// 获取字节码文件对象
		Class<?> c = Class.forName(className);

		Constructor<?> con = c.getConstructor();
		Object obj = con.newInstance();

		// public Method getMethod(String name,Class<?>... parameterTypes)
		// 第一个参数表示的方法名，第二个参数表示的是方法的参数的class类型
		Method method = c.getMethod(methodName);
		// 调用obj对象的 method 方法
		return method.invoke(obj);

	}
}
