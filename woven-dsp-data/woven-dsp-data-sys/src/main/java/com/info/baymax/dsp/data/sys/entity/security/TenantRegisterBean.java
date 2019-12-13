package com.info.baymax.dsp.data.sys.entity.security;

import org.apache.commons.lang3.StringUtils;

import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(doNotUseGetters = true, exclude = { "adminPassword" })
@EqualsAndHashCode(callSuper = false)
public class TenantRegisterBean extends Tenant implements CryptoBean {
	private static final long serialVersionUID = 7844726345988422419L;

	@ApiModelProperty("管理员密码")
	private String adminPassword;

	@ApiModelProperty("管理员分配空间容量")
	private long adminSpaceQuota = 0L;

	public TenantRegisterBean() {
	}

	@Override
	public void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
		if (StringUtils.isNotEmpty(adminPassword)) {
			adminPassword = ciphertext(adminPassword, cryptoType, cryptorDelegater);
		}
	}

	@Override
	public void decrypt(CryptorDelegater cryptorDelegater) {
		if (StringUtils.isNotEmpty(adminPassword)) {
			adminPassword = plaintext(adminPassword, cryptorDelegater);
		}
	}

}
