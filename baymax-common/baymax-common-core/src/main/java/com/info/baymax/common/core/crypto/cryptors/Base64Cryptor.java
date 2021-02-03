package com.info.baymax.common.core.crypto.cryptors;

import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.annotation.MappedCryptoType;

import java.util.Base64;

/**
 * Base64编码的字符串加密解密处理器
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
@MappedCryptoType(CryptoType.BASE64)
public class Base64Cryptor extends AbstractCryptor {

    public Base64Cryptor() {
        super("BASE64(", ")");
    }

    @Override
    String decryptCiphertext(String ciphertext, String secretKey) {
        return new String(Base64.getDecoder().decode(ciphertext));
    }

    @Override
    String encryptPlaintext(String plaintext, String secretKey) {
        return Base64.getEncoder().encodeToString(plaintext.getBytes());
    }

}
