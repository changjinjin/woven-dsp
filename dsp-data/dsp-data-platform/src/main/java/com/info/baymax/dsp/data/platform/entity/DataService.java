package com.info.baymax.dsp.data.platform.entity;

import com.info.baymax.common.entity.base.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyStringValueTypeHandler;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListFieldMappingTypeHandler;
import com.info.baymax.dsp.data.platform.bean.ApplyConfiguration;
import com.info.baymax.dsp.data.platform.bean.JobInfo;
import com.info.baymax.dsp.data.platform.mybatis.type.ClobVsApplyConfigTypeHandler;
import com.info.baymax.dsp.data.platform.mybatis.type.ClobVsJobInfoTypeHandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;

import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/16 10:14 数据服务即管理员审批通过生成的记录,供消费者调用 服务一旦生成就不会再改变,push对应的flow也就确定了
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dsp_data_service", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {
    @Index(columnList = "lastModifiedTime DESC")})
@Comment("数据服务服务信息表")
public class DataService extends BaseEntity {
    private static final long serialVersionUID = -6235196279782590695L;

    @ApiModelProperty(value = "消费者申请记录ID,与DataApplication id关联")
    @Comment("消费者申请记录ID,与DataApplication id关联")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long applicationId;

    @ApiModelProperty(value = "数据资源ID,与DataResource id关联")
    @Comment("数据资源ID,与DataResource id关联")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long dataResId;

    @ApiModelProperty(value = "服务启动类型: 0 pull, 1 push")
    @Comment("服务启动类型: 0 pull, 1 push")
    @Column(length = 1, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer type;

    @ApiModelProperty(value = "pull服务url列表")
    @Comment("push服务url列表")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String url;

    @ApiModelProperty(value = "pull服务path")
    @Comment("push服务path")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String path;

    @ApiModelProperty("调度类型：once,cron,event")
    @Comment("调度类型：once,cron,event")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String scheduleType;

    @ApiModelProperty("服务相关的一些配置,如限速限流,开始时间结束时间,周期cron信息等配置")
    @Comment("服务相关的一些配置,如限速限流,开始时间结束时间,周期cron信息等配置")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> serviceConfiguration;

    @ApiModelProperty("总的执行次数,一个服务可能被重复部署多次")
    @Comment("总的执行次数,一个服务可能被重复部署多次")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer totalExecuted;

    @ApiModelProperty("该服务总共执行的次数")
    @Comment("该服务总共执行的次数")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer executedTimes;

    @ApiModelProperty("该服务执行失败的次数")
    @Comment("该服务执行失败的次数")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer failedTimes;

    @ApiModelProperty("最近执行时间")
    @Comment("最近执行时间")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Date lastExecutedTime;

    @ApiModelProperty("该服务审核通过后是否已经部署就位,0 待部署,1 部署成功, 2 停止服务, 3 服务已过期")
    @Comment("该服务审核通过后是否已经部署就位,0 待部署,1 部署成功, 2 停止服务, 3 服务已过期")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("0")
    private Integer status;

    @ApiModelProperty("是否正在运行,由调度程序触发,0 未运行, 1 正在运行, 2 运行失败, 3 运行成功, 4 待停止, 5 已停止")
    @Comment("是否正在运行,由调度程序触发,0 未运行, 1 正在运行, 2 运行失败, 3 运行成功, 4 待停止, 5 已停止")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer isRunning;

    @ApiModelProperty("当前服务中的增量值")
    @Comment("当前服务中的增量值")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String cursorVal;

    @Lob
    @ApiModelProperty("当前服务对应的flow和schedule信息,在第一次执行时生成")
    @Comment("当前服务对应的flow和schedule信息,在第一次执行时生成")
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsJobInfoTypeHandler.class)
    private JobInfo jobInfo;

    @ApiModelProperty("所属消费者ID")
    @Comment("所属消费者ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String custId;

    @ApiModelProperty("从Application同步过来的一些配置")
    @Comment("从Application同步过来的一些配置")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsApplyConfigTypeHandler.class)
    private ApplyConfiguration applyConfiguration;

    @ApiModelProperty("特殊配置的字段及字段映射关系")
    @Comment("特殊配置的字段及字段映射关系")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListFieldMappingTypeHandler.class)
    private List<FieldMapping> fieldMappings;

    @ApiModelProperty("服务过期时间")
    @Comment("服务过期时间")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long expiredTime;// = 0L;

    @ApiModelProperty("审核意见")
    @Comment("审核意见")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String auditMind;

    @ApiModelProperty("数据类型:DATASET, DATASOURCE")
    @Comment("数据类型:DATASET, DATASOURCE")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("'DATASET'")
    private String sourceType;

}
