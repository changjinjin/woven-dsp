package com.info.baymax.common.persistence.mybatis.type.varchar;

public class VarcharVsTimeTypeHandler extends VarcharVsDateTimeTypeHandler {

    private static final String PATTERN = "HH:mm:ss";

    public VarcharVsTimeTypeHandler() {
        super(PATTERN);
    }
}
