package com.info.baymax.dsp.data.sys.entity.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.core.crypto.CryptoBean;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.common.persistence.entity.base.Maintable;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.base64.varchar.GZBase64VarcharVsListStringTypeHandler;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.validation.constraints.Phone;
import com.info.baymax.dsp.data.sys.constant.AccountStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(doNotUseGetters = true, exclude = {"password"})
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {
    @Index(columnList = "loginId"), @Index(columnList = "lastModifiedTime")})
@Comment("用户信息表")
public class User extends Maintable implements CryptoBean {
    private static final long serialVersionUID = -4066909154102918575L;

    @ApiModelProperty(value = "登录账号", required = true)
    @Comment("登录账号")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @NotBlank
    private String loginId;

    @ApiModelProperty(value = "用户密码", required = false)
    @Comment("用户密码")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String password;

    @ApiModelProperty(value = "用户手机号", required = true)
    @Comment("用户手机号")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @NotBlank
    @Phone(message = "Wrong phone number {phone}!")
    private String phone;

    @ApiModelProperty(value = "用户邮箱", required = true)
    @Comment("用户邮箱")
    @Column(length = 30)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @NotBlank
    @Email(message = "Wrong email {email}!")
    private String email;

    @ApiModelProperty(value = "密码过期时间", required = false)
    @Comment("密码过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    @Future
    private Date pwdExpiredTime;

    @ApiModelProperty(value = "账号过期时间", required = false)
    @Comment("账号过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    @Future
    private Date accountExpiredTime;

    @ApiModelProperty(value = "资源队列", required = true)
    @Comment("资源队列")
    @Lob
    @Column(length = 255)
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStringTypeHandler.class)
    private List<String> resourceQueues = new ArrayList<>();

    @ApiModelProperty(value = "HDFS空间限额", required = false)
    @Comment("HDFS空间限额")
    @Column(length = 18)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long hdfsSpaceQuota;

    @ApiModelProperty(value = "是否是超级管理员:0-否，1-是，默认0", required = false)
    @Comment("是否是超级管理员:0-否，1-是，默认0")
    @Column(name = "is_admin", length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer admin;

    @ApiModelProperty(value = "用户所属平台，默认baymax", required = false)
    @Comment("用户所属平台，默认baymax")
    @Column(name = "client_ids", length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("'baymax'")
    private String clientIds;

    @ApiModelProperty(value = "用户角色列表", required = false)
    @Transient
    private List<Role> roles;

    @ApiModelProperty(value = "权限列表", required = false, hidden = true)
    @JsonIgnore
    @Transient
    private List<SimpleGrantedAuthority> authorities;

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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (ICollections.hasNoElements(authorities) && ICollections.hasElements(roles)) {
            authorities = new ArrayList<SimpleGrantedAuthority>();
            for (Role role : roles) {
                Set<Permission> permissions = role.getPermissions();
                if (role.getEnabled() == 1 && ICollections.hasElements(permissions)) {
                    for (Permission permission : permissions) {
                        Set<RestOperation> operations = permission.getOperations();
                        if (ICollections.hasElements(operations)) {
                            authorities.addAll(operations.stream().filter(t -> t.getEnabled())
                                .map(t -> new SimpleGrantedAuthority(t.operationKey()))
                                .collect(Collectors.toList()));
                        }
                    }
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
    public void encrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        // do nothing
        if (StringUtils.isNotEmpty(password)) {
            password = cryptorDelegater.encrypt(password, secretKey, wrapped, cryptoType);
        }
    }

    @Override
    public void decrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(password)) {
            password = cryptorDelegater.decrypt(password, secretKey, wrapped, cryptoType);
        }
    }
}
