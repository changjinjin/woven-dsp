package com.info.baymax.common.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.mybatis.genid.UuidGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Maintable extends CommonEntity<String> implements Cloneable {
    private static final long serialVersionUID = -2286814072741496025L;
    // 9999-12-31
    protected static final Long MAX_DATE_TIME = 253402214400L;

    @ApiModelProperty("主键")
    @Id
    @KeySql(genId = UuidGenId.class)
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String id;

    @ApiModelProperty("过期时间戳")
    @JsonIgnore
    @Column(length = 18)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    protected Long expiredTime;

    @ApiModelProperty("版本号")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    protected Integer version;

    @ApiModelProperty("分组数")
    @Transient
    protected Long groupCount;

    @ApiModelProperty("分组属性值")
    @Transient
    protected String groupFieldValue;

    @Transient
    public Long getExpiredPeriod() {
        if (expiredTime == null || expiredTime == 0 || expiredTime.longValue() == MAX_DATE_TIME.longValue()) {
            return 0L;
        } else {
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

    public void setExpiredTime(Long expiredTime) {
        if (expiredTime == null || expiredTime == 0 || expiredTime.longValue() >= MAX_DATE_TIME.longValue()) {
            this.expiredTime = MAX_DATE_TIME;
        } else {
            this.expiredTime = expiredTime;
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
}
