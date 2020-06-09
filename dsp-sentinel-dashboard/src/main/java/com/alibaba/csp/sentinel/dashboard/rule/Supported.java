package com.alibaba.csp.sentinel.dashboard.rule;

public interface Supported {

    /**
     * 是否适配
     *
     * @param dsType datasource类型
     * @return 适配结果
     */
    default boolean supports(DsType dsType) {
        return dsType.equals(dsType());
    }

    /**
     * datasource 类型
     *
     * @return datasource 类型
     */
    DsType dsType();
}
