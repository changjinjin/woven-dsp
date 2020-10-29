package com.info.baymax.common.enums.valuable;

import java.util.Arrays;

/**
 * 可取常量枚举接口. <br>
 *
 * @author jingwei.yang
 * @date 2019-05-28 14:05
 */
public interface Valuable<T> {

	/**
	 * 说明： 获取枚举常量值. <br>
	 *
	 * @return 枚举常量值
	 */
	T getValue();

	/**
	 * 说明：判断常量值是否在常量集合中. <br>
	 *
	 * @param values
	 *            目标常量集合
	 * @return 是否目标常量集合中
	 */
	@SuppressWarnings("unchecked")
	default boolean in(T... values) {
		return Arrays.asList(values).contains(getValue());
	}

	/**
	 * 说明：判断状态值是否不在目标常量集合中. <br>
	 *
	 * @param values
	 *            目标常量集合
	 * @return 是否不在目标常量集合中
	 */
	@SuppressWarnings("unchecked")
	default boolean notIn(T... values) {
		return !in(values);
	}

}
