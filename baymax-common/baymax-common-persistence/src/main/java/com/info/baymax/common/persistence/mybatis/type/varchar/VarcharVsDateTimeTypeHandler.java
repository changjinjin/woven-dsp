package com.info.baymax.common.persistence.mybatis.type.varchar;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class VarcharVsDateTimeTypeHandler extends AbstractVarcharTypeHandler<Date> {

	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

	private String dataFormat;

	public VarcharVsDateTimeTypeHandler() {
		this(PATTERN);
	}

	protected VarcharVsDateTimeTypeHandler(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	@Override
	public String translate2Str(Date t) {
		if (t != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
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
				return DateTime.parse(result, DateTimeFormat.forPattern(dataFormat)).toDate();
			}
		}
		return null;
	}

}
