package com.info.baymax.common.service.criteria.agg;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AggType {
    AVG("avg", "求平均"), SUM("sum", "求和"), COUNT("count", "计数"), MAX("max", "最大值"), MIN("min", "最小值");

    private final String value;
    private final String desc;
}
