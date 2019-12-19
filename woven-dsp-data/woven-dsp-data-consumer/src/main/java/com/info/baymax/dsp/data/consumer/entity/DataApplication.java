package com.info.baymax.dsp.data.consumer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyStringValueTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:19
 * 消费者申请数据资源生成的记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dsp_data_application", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dataResId", "creator","transferType"})}, indexes = {
        @Index(columnList = "lastModifiedTime DESC")})
public class DataApplication extends BaseEntity {
    private static final long serialVersionUID = 8381030191792735602L;

    // 9999-12-31
    protected static Long MAX_DATE_TIME = 253402214400L;

    @ApiModelProperty(value = "数据资源ID")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String dataResId;

    @ApiModelProperty(value = "数据传输类型: 0 pull, 1 push")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer transferType;

    @ApiModelProperty("PUSH操作相关配置,关联CustDataResource获取push信息")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long custDataResourceId;

    @ApiModelProperty("PULL操作配置,关联CustApp获取接入配置信息")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long custAppId;

    @ApiModelProperty("消费者进行的一般配置信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> otherConfiguration;

    @ApiModelProperty("过期时间")
    @JsonIgnore
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long expiredTime;// = 0L;

    @ApiModelProperty("申请状态， 0: 待审批，1: 通过审批， -1:未通过审批")
    @JsonIgnore
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer status;

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
