package com.merce.woven.dsp.common.entity.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merce.woven.common.mybatis.genid.SnowflakeGenId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }, ignoreUnknown = true)
public class SnowflakeIdEntity implements Serializable {
	private static final long serialVersionUID = 8199030533526416114L;

	@ApiModelProperty("主键")
	@Id
	@KeySql(genId = SnowflakeGenId.class)
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	protected Long id;
}
