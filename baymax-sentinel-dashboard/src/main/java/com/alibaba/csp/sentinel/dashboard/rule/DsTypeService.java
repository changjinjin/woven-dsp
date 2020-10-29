package com.alibaba.csp.sentinel.dashboard.rule;

import org.springframework.beans.factory.annotation.Value;

public abstract class DsTypeService implements Supported {

    @Value("${sentinel.datasource.type:memory}")
    private String dsType;

    @Override
    public DsType dsType() {
        return DsType.valueOf(dsType);
    }

}
