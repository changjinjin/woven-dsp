package com.info.baymax.dsp.data.sys.entity.security;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.ibatis.type.JdbcType;

import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.varchar.GZBase64VarcharVsListStringTypeHandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.mybatis.mapper.annotation.ColumnType;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_tenant", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) }, indexes = {
		@Index(columnList = "lastModifiedTime") })
public class Tenant extends BaseEntity {
	private static final long serialVersionUID = -7861087791631568673L;

	@ApiModelProperty(value = "资源队列")
	@Lob
	@Column(length = 255)
	@Convert(converter = ObjectToStringConverter.class)
	@ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStringTypeHandler.class)
	protected List<String> resourceQueues;

	@ApiModelProperty(value = "HDFS空间限额")
	@Column(length = 18)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	@DefaultValue("0")
	private Long hdfsSpaceQuota;

	@ApiModelProperty(value = "全局ID")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String zid;

	@ApiModelProperty(value = "版本")
	@Column(length = 30)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String version;

	public static Tenant apply(String name, List<String> resourceQueues) {
		return new Tenant(name, resourceQueues);
	}

	public Tenant() {
	}

	public Tenant(String name) {
		this.name = name;
	}

	public Tenant(String name, List<String> resourceQueues) {
		this.name = name;
		this.resourceQueues = resourceQueues;
	}

}
