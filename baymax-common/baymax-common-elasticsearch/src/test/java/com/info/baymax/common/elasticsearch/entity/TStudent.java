package com.info.baymax.common.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.mybatis.genid.SnowflakeGenId;
import com.info.baymax.common.persistence.mybatis.type.varchar.VarcharVsDateTimeTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "t_student")
@Comment("学生表")
public class TStudent implements Idable<Long> {
	private static final long serialVersionUID = -5786555510452851869L;

	@ApiModelProperty(value = "主键")
	@Comment("主键")
	@Id
	@KeySql(genId = SnowflakeGenId.class)
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;

	@ApiModelProperty("姓名")
	@Comment("姓名")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String name;

	@ApiModelProperty("年龄")
	@Comment("年龄")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer age;

	@ApiModelProperty("生日")
	@Comment("生日")
	@Column
	@ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsDateTimeTypeHandler.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date birth;

	@ApiModelProperty("年级")
	@Comment("年级")
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String grade;

	@ApiModelProperty("班级")
	@Comment("班级")
	@Column(name = "class", length = 20)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String clazz;

	@ApiModelProperty("简介")
	@Comment("简介")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String intro;

	@ApiModelProperty("更新时间")
	@Comment("更新时间")
	@Column
	@ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsDateTimeTypeHandler.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
