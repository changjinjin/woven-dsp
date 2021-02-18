package com.info.baymax.common.core.crypto.method;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CryptoConfig {

    /**
     * 加解密秘钥
     */
    private String secretKey = "infoaeskey123456";

    /**
     * 是否包裹加密标志，默认true
     */
    private boolean wrapped = true;

}
