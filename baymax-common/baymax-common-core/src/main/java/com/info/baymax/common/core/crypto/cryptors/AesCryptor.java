package com.info.baymax.common.core.crypto.cryptors;

import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.annotation.MappedCryptoType;
import com.info.baymax.common.utils.crypto.AESUtil;

/**
 * AES加密的字符串加密解密处理器
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
@MappedCryptoType(CryptoType.AES)
public class AesCryptor extends AbstractCryptor {

    public AesCryptor() {
        super("AES(", ")");
    }

    @Override
    String decryptCiphertext(String ciphertext, String secretKey) {
        return AESUtil.decrypt(ciphertext, secretKey);
    }

    @Override
    String encryptPlaintext(String plaintext, String secretKey) {
        return AESUtil.encrypt(plaintext, secretKey);
    }

}
