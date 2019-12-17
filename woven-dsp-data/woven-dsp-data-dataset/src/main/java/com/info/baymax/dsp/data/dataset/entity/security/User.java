package com.info.baymax.dsp.data.dataset.entity.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.varchar.GZBase64VarcharVsListStringTypeHandler;
import com.info.baymax.dsp.data.dataset.entity.Maintable;
import com.info.baymax.dsp.data.dataset.utils.AccountStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(doNotUseGetters = true, exclude = { "password" })
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {
    @Index(columnList = "loginId"), @Index(columnList = "lastModifiedTime")})
public class User extends Maintable {
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    private Date pwdExpiredTime;

    @ApiModelProperty("账号过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    private Date accountExpiredTime;

    @ApiModelProperty(value = "资源队列")
    @Convert(converter = ObjectToStringConverter.class)
    @Lob
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStringTypeHandler.class)
    private List<String> resourceQueues = new ArrayList<>();

    @ApiModelProperty(value = "HDFS空间限额")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long hdfsSpaceQuota;

    @ApiModelProperty(value = "是否是超级管理员:0-否，1-是，默认0")
    @Column(name = "is_admin", length = 2)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Integer admin;

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
        return AccountStatus.ENABLED.getValue() == enabled;
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
}
