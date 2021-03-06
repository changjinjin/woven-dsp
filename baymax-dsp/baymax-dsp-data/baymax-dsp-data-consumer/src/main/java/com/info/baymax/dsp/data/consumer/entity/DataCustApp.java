package com.info.baymax.dsp.data.consumer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.persistence.entity.base.BaseEntity;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.varchar.VarcharVsStringArrayTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;

/**
 * 消费者应用信息
 *
 * @author jingwei.yang
 * @date 2019年12月20日 上午11:09:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_dc_appconfig", uniqueConstraints = {@UniqueConstraint(columnNames = {"custId", "name"})})
@Comment("消费者应用信息表")
public class DataCustApp extends BaseEntity {
    private static final long serialVersionUID = 391285825183967082L;

    @ApiModelProperty(value = "消费者ID")
    @Comment("消费者ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String custId;

    @ApiModelProperty(value = "消费者名称")
    @Comment("消费者名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String custName;

    @ApiModelProperty(value = "接入IP")
    @Comment("接入IP")
    @Convert(converter = ObjectToStringConverter.class)
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsStringArrayTypeHandler.class)
    private String[] accessIp;

    @ApiModelProperty(value = "接入KEY")
    @Comment("接入KEY")
    @Column(length = 100, unique = true)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String accessKey;

    @ApiModelProperty(value = "接口通信公钥")
    @Comment("接口通信私钥")
    @Column(length = 500)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String publicKey;

    @JsonIgnore
    @ApiModelProperty(value = "接口通信私钥")
    @Comment("接口通信私钥")
    @Column(length = 500)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String privateKey;

}
