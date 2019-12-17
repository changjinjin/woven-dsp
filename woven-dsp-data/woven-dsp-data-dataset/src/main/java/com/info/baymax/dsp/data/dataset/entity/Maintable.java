package com.info.baymax.dsp.data.dataset.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.entity.field.resolver.DefaultFieldResolver;
import com.info.baymax.common.entity.preprocess.PreEntity;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.dsp.data.dataset.entity.security.Tenant;
import com.info.baymax.dsp.data.dataset.entity.security.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class Maintable extends IdEntity implements Cloneable, PreEntity {
    private static final long serialVersionUID = 4394421573081538612L;

    // 9999-12-31
    protected static Long MAX_DATE_TIME = 253402214400L;

    @ApiModelProperty("分组计数数量")
    @Transient
    protected Long groupCount;

    @ApiModelProperty("分组属性名称")
    @Transient
    protected String groupFieldValue;

    @ApiModelProperty("名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("$unknow")
    protected String name;// = "$unknow"

    @ApiModelProperty("创建人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("SYSTEM")
    protected String creator;// = "SYSTEM";

    @ApiModelProperty("创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    protected Date createTime; // = new Date();

    @ApiModelProperty("修改人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("SYSTEM")
    protected String lastModifier;// = "SYSTEM";

    @ApiModelProperty("修改时间")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    protected Date lastModifiedTime;

    @ApiModelProperty("过期时间")
    @JsonIgnore
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    protected Long expiredTime;// = 0L;

    @ApiModelProperty("所属人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("SYSTEM")
    protected String owner;// = "SYSTEM";

    @ApiModelProperty("版本号")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("1")
    protected Integer version;// = 1;

    @ApiModelProperty("模块版本")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    protected Integer moduleVersion;// = 0;

    @ApiModelProperty("是否启用")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("1")
    protected Integer enabled;// = 1;

    @ApiModelProperty("描述简介")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String description;

    @ApiModelProperty("所属租户ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String tenantId;

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

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("can't clone " + getClass().getName(), e);
        }
    }

    public void setExpiredTime(Long expiredTime) {
        if (expiredTime == null || expiredTime == 0 || expiredTime.longValue() >= MAX_DATE_TIME.longValue()) {
            this.expiredTime = MAX_DATE_TIME;
        } else {
            this.expiredTime = expiredTime;
        }
    }

}
