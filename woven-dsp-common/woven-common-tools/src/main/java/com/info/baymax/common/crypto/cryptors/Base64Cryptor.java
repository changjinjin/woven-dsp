package com.info.baymax.common.crypto.cryptors;

import java.util.Base64;

import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.annotation.MappedCryptoType;

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
    String decryptCiphertext(String ciphertext) {
        return new String(Base64.getDecoder().decode(ciphertext));
    }

    @Override
    String encryptPlaintext(String plaintext) {
        return Base64.getEncoder().encodeToString(plaintext.getBytes());
    }

}
