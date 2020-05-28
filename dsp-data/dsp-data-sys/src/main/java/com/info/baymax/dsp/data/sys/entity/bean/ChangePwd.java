package com.info.baymax.dsp.data.sys.entity.bean;

import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class ChangePwd implements CryptoBean {

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank
    private String newPass;
    
    @ApiModelProperty(value = "老密码", required = true)
    @NotBlank
    private String oldPass;

    @Override
    public void encrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        // do nothing
    }

    @Override
    public void decrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(newPass)) {
            newPass = cryptorDelegater.decrypt(newPass, secretKey, wrapped, cryptoType);
        }
        if (StringUtils.isNotEmpty(oldPass)) {
            oldPass = cryptorDelegater.decrypt(oldPass, secretKey, wrapped, cryptoType);
        }
    }
}
