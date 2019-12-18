package com.info.baymax.dsp.data.dataset.utils;

/**
 * 账户状态枚举==> 0-未启用,1-已启用,2-已锁定,3-已禁用.
 * @author jingwei.yang
 * @date 2019年5月15日 下午6:56:53
 */
public enum AccountStatus {

    NOT_ENABLED(0, "未启用"), //
    ENABLED(1, "已启用"), //
    LOCKED(2, "已锁定"), //
    DISABLED(3, "已禁用");//

    /**
     * 数字值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String label;

    private AccountStatus(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }

}
