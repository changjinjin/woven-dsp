package com.info.baymax.common.persistence.mybatis.type.varchar;

public class VarcharVsDateTypeHandler extends VarcharVsDateTimeTypeHandler {

	private static final String PATTERN = "yyyy-MM-dd";

	public VarcharVsDateTypeHandler() {
		super(PATTERN);
	}
}
