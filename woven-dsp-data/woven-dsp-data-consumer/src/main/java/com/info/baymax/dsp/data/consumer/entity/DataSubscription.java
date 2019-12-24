package com.info.baymax.dsp.data.consumer.entity;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyStringValueTypeHandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.mybatis.mapper.annotation.ColumnType;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_data_subscription", indexes = {@Index(columnList = "lastModifiedTime DESC")})
public class DataSubscription extends BaseEntity {
    private static final long serialVersionUID = 8381030191792735602L;

    @ApiModelProperty(value = "数据资源ID")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Long dataResId;

    @ApiModelProperty(value = "数据传输类型: 0 pull, 1 push")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer transferType;

    @ApiModelProperty("PUSH操作相关配置,关联CustDataResource获取push信息")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long custDataSourceId;

    @ApiModelProperty("PULL操作配置,关联CustApp获取接入配置信息")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long custAppId;

    @ApiModelProperty("消费者进行的一般配置信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> otherConfiguration;

    @ApiModelProperty("申请状态， 0: 待审批，1: 通过审批， -1:未通过审批")
    @JsonIgnore
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer status;

}