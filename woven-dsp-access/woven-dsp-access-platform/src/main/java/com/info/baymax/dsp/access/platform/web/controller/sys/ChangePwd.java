package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@ApiModel
public class ChangePwd implements CryptoBean {

    @ApiModelProperty(value = "新密码", required = true)
    private String newPass;
    @ApiModelProperty(value = "老密码", required = true)
    private String oldPass;

    @Override
    public void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        // do nothing
    }

    @Override
    public void decrypt(CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(newPass)) {
            newPass = cryptorDelegater.decrypt(newPass);
        }
        if (StringUtils.isNotEmpty(oldPass)) {
            oldPass = cryptorDelegater.decrypt(oldPass);
        }
    }
}
