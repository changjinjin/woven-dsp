package com.info.baymax.common.service.criteria.agg;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AggType {
    AVG("avg"), SUM("sum"), COUNT("count"), MAX("max"), MIN("min");

    private final String value;
}
