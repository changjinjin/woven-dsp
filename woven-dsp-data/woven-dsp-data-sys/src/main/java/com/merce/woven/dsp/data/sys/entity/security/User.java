package com.merce.woven.dsp.data.sys.entity.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merce.woven.common.crypto.CryptoBean;
import com.merce.woven.common.crypto.CryptoType;
import com.merce.woven.common.crypto.delegater.CryptorDelegater;
import com.merce.woven.common.entity.base.BaseEntity;
import com.merce.woven.common.entity.field.DefaultValue;
import com.merce.woven.common.enums.types.YesNoType;
import com.merce.woven.common.jpa.converter.ObjectToStringConverter;
import com.merce.woven.common.mybatis.type.base64.varchar.GZBase64VarcharVsListStringTypeHandler;
import com.merce.woven.common.utils.ICollections;
import com.merce.woven.dsp.data.sys.constant.AccountStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tk.mybatis.mapper.annotation.ColumnType;

@Getter
@Setter
@ToString(doNotUseGetters = true, exclude = { "password" })
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "tenantId", "name" }) }, indexes = {
		@Index(columnList = "loginId"), @Index(columnList = "lastModifiedTime") })
public class User extends BaseEntity implements CryptoBean {
	private static final long serialVersionUID = -4066909154102918575L;

	@ApiModelProperty(value = "登录账号")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String loginId;

	@ApiModelProperty(value = "用户密码")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String password;

	@ApiModelProperty(value = "用户手机号")
	@Column(length = 11)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String phone;

	@ApiModelProperty(value = "用户邮箱")
	@Column(length = 30)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String email;

	@ApiModelProperty("密码过期时间")
	// @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date pwdExpiredTime;

	@ApiModelProperty("账号过期时间")
	// @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date accountExpiredTime;

	@ApiModelProperty(value = "资源队列")
	@Lob
	@Column(length = 255)
	@Convert(converter = ObjectToStringConverter.class)
	@ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStringTypeHandler.class)
	private List<String> resourceQueues = new ArrayList<>();

	@ApiModelProperty(value = "HDFS空间限额")
	@Column(length = 18)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	@DefaultValue("0")
	private Long hdfsSpaceQuota;

	@ApiModelProperty(value = "是否是超级管理员:0-否，1-是，默认0")
	@Column(name = "is_admin", length = 2)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@DefaultValue("0")
	private Integer admin;

	@ApiModelProperty(value = "用户角色列表")
	@Transient
	private List<Role> roles;

	@JsonIgnore

	@Transient
	private List<String> authorities;

	public User() {
		this.resourceQueues.add("default");
	}

	public User(String name, String loginId, String password) {
		this();
		this.name = name;
		this.loginId = loginId;
		this.password = password;
	}

	public User(String loginId, String password, boolean isAdmin) {
		this();
		this.name = loginId;
		this.loginId = loginId;
		this.password = password;
		this.admin = isAdmin ? 1 : 0;
	}

	public static User apply(String name, String loginId, String password) {
		return new User(name, loginId, password);
	}

	public static User apply(String loginId, String password, boolean isAdmin) {
		return new User(loginId, password, isAdmin);
	}

	public Collection<String> getAuthorities() {
		if (ICollections.hasNoElements(authorities) && ICollections.hasElements(roles)) {
			authorities = new ArrayList<String>();
			for (Role role : roles) {
				Set<Permission> permissions = role.getPermissions();
				if (role.getEnabled() == 1 && ICollections.hasElements(permissions)) {
					authorities.addAll(permissions.stream().filter(t -> t.getEnabled() == 1).map(t -> t.getUrl())
							.collect(Collectors.toList()));
				}
			}
		}
		return authorities;
	}

	@Transient
	@JsonIgnore
	public String getUsername() {
		return loginId;
	}

	@Transient
	@JsonIgnore
	public boolean isAccountNonExpired() {
		if (admin())
			return true;
		return accountExpiredTime != null && LocalDateTime.now()
				.isBefore(LocalDateTime.ofInstant(accountExpiredTime.toInstant(), ZoneId.systemDefault()));
	}

	@Transient
	@JsonIgnore
	public boolean isAccountNonLocked() {
		if (admin())
			return true;
		return AccountStatus.ENABLED.equalsTo(enabled);
	}

	@Transient
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		if (admin())
			return true;
		return pwdExpiredTime != null && LocalDateTime.now()
				.isBefore(LocalDateTime.ofInstant(pwdExpiredTime.toInstant(), ZoneId.systemDefault()));
	}

	@Transient
	public boolean admin() {
		return admin != null && YesNoType.YES.equalsTo(admin);
	}

	@Override
	public void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
		// do nothing
		if (StringUtils.isNotEmpty(password)) {
			password = cryptorDelegater.encrypt(cryptoType, password);
		}
	}

	@Override
	public void decrypt(CryptorDelegater cryptorDelegater) {
		if (StringUtils.isNotEmpty(password)) {
			password = cryptorDelegater.decrypt(password);
		}
	}
}
