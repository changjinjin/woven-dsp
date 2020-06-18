package com.info.baymax.dsp.access.dataapi.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Setter
@Getter
public final class MetricsEntity {
	public static final String TYPE_NAME = "metrics_entity";
    private String title;
    private int weight;
    private Date birth;
}