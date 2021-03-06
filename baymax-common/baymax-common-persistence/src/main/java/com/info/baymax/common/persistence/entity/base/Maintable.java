package com.info.baymax.common.persistence.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.persistence.mybatis.genid.UuidGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }, ignoreUnknown = true)
public class Maintable extends CommonEntity<String> implements Cloneable {
	private static final long serialVersionUID = -2286814072741496025L;
	// 9999-12-31
	public static final Long MAX_DATE_TIME = 253402214400L;

	@ApiModelProperty("主键")
	@Comment("主键")
	@Id
	@org.springframework.data.annotation.Id
	@KeySql(genId = UuidGenId.class)
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Keyword)
	protected String id;

	@ApiModelProperty("过期时间戳")
    @Comment("过期时间戳")
    @JsonIgnore
    @Column(length = 18)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @Field(name = "expired_time", type = FieldType.Long)
    protected Long expiredTime;

	@ApiModelProperty("版本号")
	@Comment("版本号")
	@Column(length = 11)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@Field(type = FieldType.Integer)
	protected Integer version;

	@ApiModelProperty("分组数")
	@Transient
	protected Long groupCount;

	@ApiModelProperty("分组属性值")
	@Transient
	protected String groupFieldValue;

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
