package com.merce.woven.common.crypto.cryptors;

import com.merce.woven.common.crypto.CryptoType;
import com.merce.woven.common.crypto.annotation.MappedCryptoType;
import com.merce.woven.common.utils.AesUtils;

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
    String decryptCiphertext(String ciphertext) {
        return AesUtils.aesDecrypt(ciphertext);
    }

    @Override
    String encryptPlaintext(String plaintext) {
        return AesUtils.aesEncrypt(plaintext);
    }

}
