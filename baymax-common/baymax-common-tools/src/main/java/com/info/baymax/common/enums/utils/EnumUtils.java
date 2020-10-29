package com.info.baymax.common.enums.utils;

import java.util.HashMap;
import java.util.Map;

import com.info.baymax.common.enums.valuable.ValuableAndComparableAndLabelable;

/**
 * 枚举工具类
 *
 * @author jingwei.yang
 * @date 2019-05-28 14:05
 */
public class EnumUtils {

	/**
	 * 获取枚举常量值和标签值
	 *
	 * @param values
	 *            枚举列表
	 * @return 枚举常量值和标签值映射
	 */
	public static <V extends Enum<V> & ValuableAndComparableAndLabelable<?>> Map<Object, String> values(V[] values) {
		Map<Object, String> map = new HashMap<>();
		for (V t : values) {
			map.put(t.getValue(), t.getLabel());
		}
		return map;
	}

	/**
	 * 获取枚举常量值和标签值
	 *
	 * @param clazz
	 *            枚举类
	 * @return 枚举常量值和标签值映射
	 */
	public static <T> Map<Object, String> values(Class<? extends ValuableAndComparableAndLabelable<?>> clazz) {
		Map<Object, String> map = new HashMap<>();
		ValuableAndComparableAndLabelable<?>[] enumConstants = clazz.getEnumConstants();
		for (ValuableAndComparableAndLabelable<?> t : enumConstants) {
			map.put(t.getValue(), t.getLabel());
		}
		return map;
	}
}
