package com.merce.woven.common.enums.types;

import com.merce.woven.common.enums.valuable.IntegerValuable;
/**
 * 真假枚举: 0-否,1-是
 *
 * @author jingwei.yang
 * @date 2019-05-28 14:03
 */
public enum YesNoType implements IntegerValuable {
	NO(0, "否", false), //
	YES(1, "是", true); //

	private final Integer value;// 数字值
	private final String label;// 类型名称
	private final Boolean result;// 是否为真

	private YesNoType(Integer value, String label, boolean result) {
		this.value = value;
		this.label = label;
		this.result = result;
	}

	public String getLabel() {
		return label;
	}

	public Integer getValue() {
		return value;
	}

	public Boolean getResult() {
		return result;
	}

	public static YesNoType valueOf(Integer value) {
		switch (value) {
		case 1:
			return YesNoType.YES;
		default:
			return YesNoType.NO;
		}
	}

	public static YesNoType valueOf(boolean result) {
		return result ? YesNoType.YES : YesNoType.NO;
	}

}
