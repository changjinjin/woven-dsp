package com.info.baymax.common.enums.valuable;

/**
 * 可取常量且可比较大小常量值枚举接口. <br>
 *
* @author jingwei.yang
 * @date 2019-05-28 14:05
 */
public interface ValuableAndComparable<T extends Comparable<T>> extends Valuable<T> {

	/**
	 * 常量值与目标常量值比较结果. <br>
	 *
	 * @param value
	 *            目标常量值
	 * @return 比较结果
	 * @throws Exception
	 */
	default int compareTo(T value) throws NullPointerException {
		if (value != null) {
			return getValue().compareTo(value);
		}
		throw new NullPointerException("Contrast value is empty.");
	}

	/**
	 * 常量值与目标常量值是否相等.<br>
	 *
	 * @param value
	 *            目标常量值
	 * @return 是否相等
	 */
	default boolean equalsTo(T value) {
		try {
			return compareTo(value) == 0;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 常量值与目标常量值是否不相等. <br>
	 *
	 * @param value
	 *            目标常量值
	 * @return 是否不相等
	 * @throws Exception
	 */
	default boolean notEqualsTo(T value) {
		return !equalsTo(value);
	}

	/**
	 * 判断常量值是否是大于目标常量值. <br>
	 *
	 * @param value
	 *            目标常量值
	 * @return 是否大于
	 */
	default boolean greaterThan(T value) {
		try {
			return compareTo(value) > 0;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 判断常量值是否是大于等于目标常量值. <br>
	 *
	 * @param value
	 *            目标常量值
	 * @return 是否大于等于
	 */
	default boolean greaterThanOrEqualTo(T value) {
		try {
			return compareTo(value) >= 0;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 判断常量值是否是小于目标常量值. <br>
	 *
	 * @param value
	 *            目标常量值
	 * @return 是否小于
	 */
	default boolean lessThan(T value) {
		try {
			return compareTo(value) < 0;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 判断常量值是否是小于等于目标常量值. <br>
	 *
	 * @param value
	 *            目标常量值
	 * @return 是否小于等于
	 */
	default boolean lessThanOrEqualTo(T value) {
		try {
			return compareTo(value) <= 0;
		} catch (NullPointerException e) {
			return false;
		}
	}

}
