package com.info.baymax.common.enums.types;

import com.info.baymax.common.enums.valuable.ByteValuable;

/**
 *  使用状态：0-启用、1-闲置.
 * @author jingwei.yang
 * @date 2019-05-28 14:02
 */
public enum UsingStatus implements ByteValuable {

	ENABLE((byte) 0, "启用"), //
	DISABLE((byte) 1, "闲置"); //

	private final String label;// 类型名称
	private final Byte value;// 类型值

	private UsingStatus(Byte value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public Byte getValue() {
		return value;
	}

}
