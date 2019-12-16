package com.info.baymax.dsp.data.consumer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;

import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.entity.base.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tk.mybatis.mapper.annotation.ColumnType;

@Getter
@Setter
@ToString(doNotUseGetters = true, exclude = {"password"})
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_consumer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "username"})}, indexes = {
    @Index(columnList = "lastModifiedTime")})
public class Consumer extends BaseEntity implements CryptoBean {
    private static final long serialVersionUID = -3170541763416732171L;

    @ApiModelProperty(value = "用户名")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String username;

    @ApiModelProperty(value = "用户密码")
    @Column(length = 150)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String password;

    @Override
    public void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
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