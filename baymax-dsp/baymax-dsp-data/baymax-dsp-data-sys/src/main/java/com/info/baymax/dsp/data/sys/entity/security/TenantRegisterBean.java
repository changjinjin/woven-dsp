package com.info.baymax.dsp.data.sys.entity.security;

import com.info.baymax.common.core.crypto.CryptoBean;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
@ToString(doNotUseGetters = true, exclude = {"adminPassword"})
@EqualsAndHashCode(callSuper = false)
public class TenantRegisterBean extends Tenant implements CryptoBean {
    private static final long serialVersionUID = 7844726345988422419L;

    @ApiModelProperty("管理员密码")
    private String adminPassword;

    @ApiModelProperty("管理员分配空间容量")
    private long adminSpaceQuota = 0L;

    public static TenantRegisterBean apply(String id, List<String> resourceQueues) {
        return new TenantRegisterBean(id, resourceQueues);
    }

    public TenantRegisterBean() {
    }

    public TenantRegisterBean(String id, List<String> resourceQueues) {
        this.id = id;
        this.name = id;
        this.resourceQueues = resourceQueues;
    }

    @Override
    public void encrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(adminPassword)) {
            adminPassword = ciphertext(adminPassword, secretKey, wrapped, cryptoType, cryptorDelegater);
        }
    }

    @Override
    public void decrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(adminPassword)) {
            adminPassword = plaintext(adminPassword, secretKey, wrapped, cryptoType, cryptorDelegater);
        }
    }

}
