package com.info.baymax.common.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.mybatis.genid.SnowflakeGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
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
public class TStudent implements Idable<Long> {
	private static final long serialVersionUID = -5786555510452851869L;

	@ApiModelProperty(value = "主键")
	@Comment("主键")
	@Id
	@org.springframework.data.annotation.Id
	@KeySql(genId = SnowflakeGenId.class)
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	@Field(type = FieldType.Keyword)
	private Long id;

	@ApiModelProperty("姓名")
	@Comment("姓名")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Text)
	private String name;

	@ApiModelProperty("年龄")
	@Comment("年龄")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@Field(type = FieldType.Integer)
	private Integer age;

	@ApiModelProperty("性别:M-男，F-女")
	@Comment("性别:M-男，F-女")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Keyword)
	@ColumnDefault("'F'")
	private String gender;

	@ApiModelProperty("生日")
	@Comment("生日")
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Field(type = FieldType.Date)
	private Date birth;

	@ApiModelProperty("年级")
	@Comment("年级")
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Text)
	private String grade;

	@ApiModelProperty("班级")
	@Comment("班级")
	@Column(name = "clazz", length = 20)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Text)
	private String clazz;

	@ApiModelProperty("简介")
	@Comment("简介")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Text)
	private String intro;

	@ApiModelProperty("更新时间")
	@Comment("更新时间")
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Field(name = "update_time", type = FieldType.Date)
	private Date updateTime;
}
