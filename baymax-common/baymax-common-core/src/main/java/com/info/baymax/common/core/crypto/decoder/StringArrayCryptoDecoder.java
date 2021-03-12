package com.info.baymax.common.core.crypto.decoder;

import com.info.baymax.common.core.crypto.CryptoDecoder;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;

import java.util.Arrays;

/**
 * 默认的无处理的数据解密器
 *
 * @param <T> 解密数据类型
 * @author jingwei.yang
 * @date 2021年3月10日 下午4:59:28
 */
public class StringArrayCryptoDecoder implements CryptoDecoder<String[]> {

    @Override
    public String[] decode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType,
                           CryptorDelegater cryptorDelegater) {
        String[] arr = (String[]) obj;
        if (arr != null && arr.length > 0) {
            return Arrays.stream(arr).map(t -> cryptorDelegater.decrypt(t, secretKey, wrapped, cryptoType))
                .toArray(String[]::new);
        }
        return arr;
    }

}
