package com.info.baymax.dsp.data.platform.bean;

import com.info.baymax.common.core.enums.valuable.IntegerValuable;

/**
 * 数据传输方式枚举
 *
 * @author jingwei.yang
 * @date 2020年4月28日 下午5:12:50
 */
public enum TransferType implements IntegerValuable {
    PULL(0, "拉取"), //
    PUSH(1, "推送"); //

    /**
     * 数字值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String label;

    private TransferType(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

}
