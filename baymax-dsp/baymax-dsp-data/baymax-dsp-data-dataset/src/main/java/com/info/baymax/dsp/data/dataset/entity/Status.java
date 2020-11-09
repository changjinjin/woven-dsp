package com.info.baymax.dsp.data.dataset.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merce.woven.common.ConfigObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class Status implements Serializable {
    private static final long serialVersionUID = -4557436931290419940L;

    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("开始时间")
    private long startTime;
    @ApiModelProperty("结束时间")
    private long endTime;
    @ApiModelProperty("预计总数")
    private long estimateTotal;
    @ApiModelProperty("进程ID")
    private long processed;
    @ApiModelProperty("耗时毫秒数")
    private long cost;
    @ApiModelProperty("预计耗时毫秒数")
    private long estimateCost;
    @ApiModelProperty("其他配置")
    private ConfigObject others = new ConfigObject();

    public static enum StatusType {
        RUNNING, UNKNOWN, FAILED, SUCCEEDED, KILLED, READY, UNAVAILABLE, TIMEOUT
    }

    public Status() {
        this(StatusType.UNKNOWN.toString(), 0L, 0L, 0L, 0L, 0L, 0L, null);
    }

    public Status(String type, long estimateTotal, long processed, long cost, long estimateCost) {
        this(type, 0L, 0L, estimateTotal, processed, cost, estimateCost, new ConfigObject());
    }

    public Status(String type, long estimateTotal, long processed, long cost, long estimateCost, ConfigObject others) {
        this(type, 0L, 0L, estimateTotal, processed, cost, estimateCost, others);
    }

    public Status(String type, Long startTime, Long endTime, Long estimateTotal, Long processed, Long cost,
                  Long estimateCost, ConfigObject others) {
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.estimateTotal = estimateTotal;
        this.processed = processed;
        this.cost = cost;
        this.estimateCost = estimateCost;
        this.others = others;
    }

    @Transient
    @JsonIgnore
    public boolean isComplete() {
        return type.equals(StatusType.KILLED.toString()) || type.equals(StatusType.SUCCEEDED.toString())
            || type.equals(StatusType.FAILED.toString());
    }
}
