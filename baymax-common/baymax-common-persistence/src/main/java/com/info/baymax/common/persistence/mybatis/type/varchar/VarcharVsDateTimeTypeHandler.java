package com.info.baymax.common.persistence.mybatis.type.varchar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VarcharVsDateTimeTypeHandler extends AbstractVarcharTypeHandler<Date> {

	private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	@Override
	public String translate2Str(Date t) {
		if (t != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
			return sdf.format(t);
		}
		return null;
	}

	@Override
	public Date translate2Bean(String result) {
		if (!result.isEmpty()) {
			try {
				return DateTime.parse(result).toDate();
			} catch (Exception e) {
				return DateTime.parse(result, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS")).toDate();
			}
		}
		return null;
	}

}
