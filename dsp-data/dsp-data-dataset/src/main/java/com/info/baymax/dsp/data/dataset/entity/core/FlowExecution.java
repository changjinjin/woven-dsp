package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.entity.base.Maintable;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.dsp.data.dataset.entity.ConfigItem;
import com.info.baymax.dsp.data.dataset.entity.ConfigObject;
import com.info.baymax.dsp.data.dataset.entity.Status;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListConfigItemTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.varchar.GZBase64VarcharVsConfigObjectTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.varchar.GZBase64VarcharVsListStepExecutionTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.varchar.GZBase64VarcharVsStatusTypeHandler;
import com.info.baymax.dsp.data.dataset.utils.ValueBind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_flow_execution", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "owner"})}, indexes = {@Index(columnList = "jobId"),
    @Index(columnList = "flowId"), @Index(columnList = "flowSchedulerId"),
    @Index(columnList = "lastModifiedTime DESC")})
public class FlowExecution extends Maintable {
    private static final long serialVersionUID = 1975744368876038158L;

    @ApiModelProperty("来源")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String source;

    @ApiModelProperty("")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("NiL")
    private String externalId;

    @ApiModelProperty("任务ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String jobId;

    @ApiModelProperty("排序序号")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer orderNum;

    @ApiModelProperty("流程ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowId;

    @ApiModelProperty("流程版本号")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("-1")
    private Integer flowVersion;// -1 mean max version

    @ApiModelProperty("流程名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowName;

    @ApiModelProperty("流程类型：dataflow, workflow, streamflow")
    @ValueBind(FieldType = ValueBind.fieldType.STRING, EnumStringValues = {"dataflow", "workflow", "streamflow"})
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowType;

    @ApiModelProperty("流程调度信息ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowSchedulerId;

    @ApiModelProperty("流程调度信息名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowSchedulerName;

    @ApiModelProperty("spark application ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String appId;

    @ApiModelProperty("flow提交后生成ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String fid;

    @ApiModelProperty("服务进程号")
    @Column(length = 10)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String pid;

    /**
     * Flow execution metrics info
     */
    @ApiModelProperty("输入字节数")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long inputBytes;
    @ApiModelProperty("输出字节数")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long outputBytes;
    @ApiModelProperty("输入数据记录数")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long inputRecords;
    @ApiModelProperty("输出数据记录数")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long outputRecords;

    @ApiModelProperty("状态详情")
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsStatusTypeHandler.class)
    private Status status;

    @ApiModelProperty("状态类型")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String statusType;

    @ApiModelProperty("耗时毫秒数")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long cost;

    @ApiModelProperty("执行步骤信息")
    @Column(length = 2000)
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStepExecutionTypeHandler.class)
    private List<StepExecution> steps;

    @ApiModelProperty("参数信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListConfigItemTypeHandler.class)
    private List<ConfigItem> arguments;

    @ApiModelProperty("属性信息")
    @Column(length = 2000)
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsConfigObjectTypeHandler.class)
    private ConfigObject properties;

    @ApiModelProperty("扩张参数信息")
    @Column(length = 2000)
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsConfigObjectTypeHandler.class)
    private ConfigObject extraConfigurations;

    @ApiModelProperty("依赖参数")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String dependencies;

    @ApiModelProperty("所属日期")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String day;

    @ApiModelProperty("所属小时")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String hour;

    public FlowExecution() {
    }

    public FlowExecution(String name, int orderNum, String fshId, String fshName, String flowId, String flowName,
                         String flowType, Status status, List<StepExecution> steps) {
        this(name, "", orderNum, fshId, fshName, flowId, flowName, flowType, status, steps,
            new ArrayList<ConfigItem>());
    }

    public FlowExecution(long groupCount, String groupFieldValue) {
        this.groupCount = groupCount;
        this.groupFieldValue = groupFieldValue;
    }

    public FlowExecution(String name, String source, int orderNum, String fshId, String fshName, String flowId,
                         String flowName, String flowType, Status status, List<StepExecution> steps) {
        this(name, source, orderNum, fshId, fshName, flowId, flowName, flowType, status, steps,
            new ArrayList<ConfigItem>());
    }

    public FlowExecution(String name, int orderNum, String fshId, String fshName, String flowId, String flowName,
                         String flowType, Status status, List<StepExecution> steps, List<ConfigItem> arguments) {
        this(name, "", orderNum, fshId, fshName, flowId, flowName, flowType, status, steps, arguments);
    }

    public FlowExecution(String name, int orderNum, String fshId, String fshName, String flowId, String flowName,
                         String flowType, Status status, List<StepExecution> steps, List<ConfigItem> arguments, String statusType) {
        this(name, "", orderNum, fshId, fshName, flowId, flowName, flowType, status, steps, arguments, statusType);
    }

    public FlowExecution(String name, String source, int orderNum, String fshId, String fshName, String flowId,
                         String flowName, String flowType, Status status, List<StepExecution> steps, List<ConfigItem> arguments,
                         String statusType) {
        this.statusType = statusType;
        this.name = name;
        this.source = source;
        this.orderNum = orderNum;
        this.flowName = flowName;
        this.flowId = flowId;
        this.flowType = flowType;
        this.status = status;
        this.flowSchedulerId = fshId;
        this.flowSchedulerName = fshName;
        this.steps = steps;
        this.arguments = arguments;

        Date createTime = this.getCreateTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("yyyyMMddHH");
        this.day = dayFormat.format(createTime);
        this.hour = hourFormat.format(createTime);

    }

    public FlowExecution(String name, String source, int orderNum, String fshId, String fshName, String flowId,
                         String flowName, String flowType, Status status, List<StepExecution> steps, List<ConfigItem> arguments) {
        this.name = name;
        this.source = source;
        this.orderNum = orderNum;
        this.flowName = flowName;
        this.flowId = flowId;
        this.flowType = flowType;
        this.status = status;
        this.flowSchedulerId = fshId;
        this.flowSchedulerName = fshName;
        this.steps = steps;
        this.arguments = arguments;

        Date createTime = this.getCreateTime();
        if (createTime == null) {
            createTime = new Date();
        }
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("yyyyMMddHH");
        this.day = dayFormat.format(createTime);
        this.hour = hourFormat.format(createTime);

    }

    public void setStatus(Status status) {
        synchronized (this) {
            if (this.statusType != null) {
                if (!isCompletedForStatus()) {
                    this.status = status;
                }
            } else {
                this.status = status;
            }
        }
    }

    public void setStatusType(String statusType) {
        synchronized (this) {
            if (this.statusType != null) {
                if (!isCompleted() && !statusType.equalsIgnoreCase(this.statusType)) {
                    this.statusType = statusType;
                }
            } else {
                this.statusType = statusType;
            }
        }
    }

    @Transient
    public boolean isCompleted() {
        Status.StatusType status = Status.StatusType.valueOf(statusType);
        return status == Status.StatusType.SUCCEEDED || status == Status.StatusType.FAILED
            || status == Status.StatusType.KILLED;
    }

    @Transient
    private boolean isCompletedForStatus() {
        Status.StatusType statusType = Status.StatusType.valueOf(status.getType());
        return statusType == Status.StatusType.SUCCEEDED || statusType == Status.StatusType.FAILED
            || statusType == Status.StatusType.KILLED;
    }
}
