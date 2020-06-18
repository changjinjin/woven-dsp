package com.info.baymax.data.elasticsearch.repository;

import io.searchbox.annotations.JestId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString(doNotUseGetters = true)
public final class MetricsEntity {
    @JestId
    private String id;
    private String sid;
    private String title;
    private int weight;
}