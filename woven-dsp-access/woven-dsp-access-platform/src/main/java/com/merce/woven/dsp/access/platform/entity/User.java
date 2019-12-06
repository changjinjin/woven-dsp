package com.merce.woven.dsp.access.platform.entity;

import com.merce.woven.common.entity.base.SnowFlakeIdEntity;
import com.merce.woven.common.entity.field.DefaultValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "t_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) }, indexes = {
		@Index(columnList = "realName") })
public class User extends SnowFlakeIdEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("账号")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String username;

	@ApiModelProperty("姓名")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String realName;

	@ApiModelProperty("密码")
	@Column(length = 150)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String password;

	@ApiModelProperty("性别：0-男，1-女，2-未知")
	@Column(length = 1)
	@DefaultValue("0")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer gender;

	@ApiModelProperty("生日：yyyy-MM-dd")
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date birth;

	@ApiModelProperty("年龄")
	@Column(length = 3)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer age;

}
