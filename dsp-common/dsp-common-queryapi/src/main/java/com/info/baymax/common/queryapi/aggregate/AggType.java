package com.info.baymax.common.queryapi.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AggType {
    COUNT("COUNT", "计数"), AVG("AVG", "求平均"), SUM("SUM", "求和"), MAX("MAX", "最大值"), MIN("MIN", "最小值");

    private final String value;
    private final String desc;
}
