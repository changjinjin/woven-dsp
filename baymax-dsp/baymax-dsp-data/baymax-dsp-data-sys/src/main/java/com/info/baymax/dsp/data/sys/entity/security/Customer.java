package com.info.baymax.dsp.data.sys.entity.security;

import com.info.baymax.common.core.crypto.CryptoBean;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.persistence.entity.base.Maintable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(doNotUseGetters = true, exclude = {"password"})
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_customer", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "username"})})
@Comment("消费者信息表")
public class Customer extends Maintable implements CryptoBean {
    private static final long serialVersionUID = -3170541763416732171L;

    @ApiModelProperty(value = "用户名", required = true)
    @Comment("用户名")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @NotBlank(message = "Username could not be blank.")
    private String username;

    @ApiModelProperty(value = "用户密码", required = false)
    @Comment("用户密码")
    @Column(length = 150)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String password;

    @Override
    public void encrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
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
