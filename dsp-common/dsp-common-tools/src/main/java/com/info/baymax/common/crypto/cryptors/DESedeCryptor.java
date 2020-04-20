package com.info.baymax.common.crypto.cryptors;

import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.annotation.MappedCryptoType;
import com.info.baymax.common.utils.crypto.TripDESUtil;

/**
 * TripDES加密的字符串加密解密处理器
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
@MappedCryptoType(CryptoType.DESEDE)
public class DESedeCryptor extends AbstractCryptor {

    public DESedeCryptor() {
        super("DESEDE(", ")");
    }

    @Override
    String decryptCiphertext(String ciphertext, String secretKey) {
        return TripDESUtil.decrypt(ciphertext, secretKey);
    }

    @Override
    String encryptPlaintext(String plaintext, String secretKey) {
        return TripDESUtil.encrypt(plaintext, secretKey);
    }

}
