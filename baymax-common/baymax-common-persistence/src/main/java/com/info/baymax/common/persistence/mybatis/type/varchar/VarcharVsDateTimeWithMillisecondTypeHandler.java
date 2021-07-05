package com.info.baymax.common.persistence.mybatis.type.varchar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VarcharVsDateTimeWithMillisecondTypeHandler extends VarcharVsDateTimeTypeHandler {

	private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public VarcharVsDateTimeWithMillisecondTypeHandler() {
		super(PATTERN);
	}
}
