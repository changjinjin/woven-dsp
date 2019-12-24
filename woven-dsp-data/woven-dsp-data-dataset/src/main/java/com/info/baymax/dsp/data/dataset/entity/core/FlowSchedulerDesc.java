package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.entity.base.Maintable;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.dsp.data.dataset.entity.ConfigObject;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsConfigObjectTypeHandler;
import com.info.baymax.dsp.data.dataset.utils.ValueBind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_flow_schedule", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "owner"})}, indexes = {@Index(columnList = "schedulerId"),
    @Index(columnList = "createTime")})
public class FlowSchedulerDesc extends Maintable {
    private static final long serialVersionUID = 1655214158184860977L;

    @ApiModelProperty("来源")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String source;

    @ApiModelProperty("调度类型：once,cron")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String schedulerId;

    @ApiModelProperty("流程ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowId;

    @ApiModelProperty("流程名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowName;

    @ApiModelProperty("流程类型：dataflow, workflow, streamflow")
    @ValueBind(FieldType = ValueBind.fieldType.STRING, EnumStringValues = {"dataflow", "workflow", "streamflow"})
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowType;

    @ApiModelProperty("流程版本号")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer flowVersion;// -1 mean max version

    @ApiModelProperty("所属日期")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String day;

    @ApiModelProperty("所属小时")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String hour;

    /**
     * arguments: flow runtime arguments which is related to flow parameters one by
     * one properties: runtime properties
     */
    @ApiModelProperty("配置信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsConfigObjectTypeHandler.class)
    private ConfigObject configurations;

    @ApiModelProperty("总的执行次数")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("0")
    private Integer totalExecuted;

    @ApiModelProperty("最近执行时间")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Date lastExecutedTime;

    public FlowSchedulerDesc() {
    }

    public FlowSchedulerDesc(long groupCount, String groupFieldValue) {
        this.groupCount = groupCount;
        this.groupFieldValue = groupFieldValue;
    }

    public FlowSchedulerDesc(String name, String schedulerId, String flowId, String flowName,
                             ConfigObject configurations) {
        this(name, "", schedulerId, flowId, -1, flowName, configurations, null);
    }

    public FlowSchedulerDesc(String name, String source, String schedulerId, String flowId, String flowName,
                             ConfigObject configurations) {
        this(name, source, schedulerId, flowId, -1, flowName, configurations, null);
    }

    public FlowSchedulerDesc(String name, String schedulerId, String flowId, String flowName,
                             ConfigObject configurations, String flowType) {
        this(name, "", schedulerId, flowId, -1, flowName, configurations, flowType);
    }

    public FlowSchedulerDesc(String name, String source, String schedulerId, String flowId, int flowVersion,
                             String flowName, ConfigObject configurations, String flowType) {
        this.name = name;
        this.source = source;
        this.schedulerId = schedulerId;
        this.flowId = flowId;
        this.flowVersion = flowVersion;
        this.flowName = flowName;
        this.flowType = flowType;
        this.configurations = configurations;

        Date date = this.getCreateTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("yyyyMMddHH");
        this.day = dayFormat.format(date);
        this.hour = hourFormat.format(date);
    }
}
