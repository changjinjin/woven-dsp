package com.info.baymax.dsp.data.consumer.entity;

import com.info.baymax.common.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_cust_app", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "username"})})
public class CustApp extends BaseEntity {
    private static final long serialVersionUID = 391285825183967082L;

    @ApiModelProperty(value = "消费者ID")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long custId;

    @ApiModelProperty(value = "消费者ID")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Long custName;

    @ApiModelProperty(value = "应用名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String appName;

    @ApiModelProperty(value = "接入IP")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String accessIp;

    @ApiModelProperty(value = "接入KEY")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String accessKey;

}
