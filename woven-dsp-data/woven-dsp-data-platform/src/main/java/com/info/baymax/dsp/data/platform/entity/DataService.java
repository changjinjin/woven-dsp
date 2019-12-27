package com.info.baymax.dsp.data.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyStringValueTypeHandler;
import com.info.baymax.dsp.data.platform.bean.JobInfo;
import com.info.baymax.dsp.data.platform.bean.TransformRule;
import com.info.baymax.dsp.data.platform.mybatis.mapper.type.ClobVsJobInfoTypeHandler;
import com.info.baymax.dsp.data.platform.mybatis.mapper.type.ClobVsMapStringKeyTransformRuleValueTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/16 10:14 数据服务即管理员审批通过生成的记录,供消费者调用
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dsp_data_service", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {
    @Index(columnList = "lastModifiedTime DESC")})
public class DataService extends BaseEntity {
    private static final long serialVersionUID = -6235196279782590695L;

    @ApiModelProperty(value = "消费者申请记录ID,与DataApplication id关联")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long applicationId;

    @ApiModelProperty(value = "消费者ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String custId;

    @ApiModelProperty(value = "服务启动类型: 0 pull, 1 push")
    @Column(length = 1, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer type;

    @ApiModelProperty(value = "pull服务url列表")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String url;

    @ApiModelProperty(value = "pull服务path")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String path;

    @ApiModelProperty(value = "pull服务参数说明")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> pullConfiguration;

    @ApiModelProperty("调度类型：once,cron,event")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String scheduleType;

    @ApiModelProperty("数据字段相关配置,包括加密,脱敏,转换等")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsMapStringKeyTransformRuleValueTypeHandler.class)
    private Map<String, TransformRule> fieldConfiguration;

    @ApiModelProperty("服务相关的一些配置,如限速限流,开始时间结束时间,周期cron信息等配置")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> serviceConfiguration;

    @ApiModelProperty("总的执行次数,一个服务可能被重复部署多次")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer totalExecuted;

    @ApiModelProperty("本次部署后执行的次数,一个服务可能被重复部署多次,每次部署成功后此参数要恢复为0")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer executedTimes;

    @ApiModelProperty("本次部署后执行失败的次数")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer failedTimes;

    @ApiModelProperty("最近执行时间")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Date lastExecutedTime;

    @ApiModelProperty("该服务审核通过后是否已经部署就位,0 待部署,1 部署成功, 2 停止服务")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("0")
    private Integer status;

    @ApiModelProperty("是否正在运行,由调度程序触发,0 未运行, 1 正在运行, 2 运行失败, 3 运行成功")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer isRunning;

    @ApiModelProperty("当前服务中的增量值")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String cursorVal;

    @Lob
    @ApiModelProperty("当前服务对应的flow和schedule信息,在第一次执行时生成")
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsJobInfoTypeHandler.class)
    private JobInfo jobInfo;

    @ApiModelProperty("服务过期时间")
    @JsonIgnore
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long expiredTime;// = 0L;
    // 9999-12-31
    protected static Long MAX_DATE_TIME = 253402214400L;

    @Transient
    public Long getExpiredPeriod() {
        if (expiredTime == null || expiredTime == 0 || expiredTime.longValue() >= MAX_DATE_TIME.longValue()) {
            return 0L;
        } else {
            if (createTime == null) {
                createTime = new Date();
            }
            return expiredTime - createTime.getTime() / 1000;
        }
    }

    public void setExpiredPeriod(Long expiredPeriod) {
        if (expiredPeriod == 0) {
            expiredTime = MAX_DATE_TIME;
        } else {
            if (createTime == null) {
                createTime = new Date();
            }
            expiredTime = createTime.getTime() / 1000 + expiredPeriod;
        }
    }
}
