package com.info.baymax.data.elasticsearch.jest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AggType {
	COUNT("count", "计数"), AVG("avg", "求平均"), SUM("sum", "求和"), MAX("max", "最大值"), MIN("min", "最小值");

	private final String value;
	private final String desc;
}
