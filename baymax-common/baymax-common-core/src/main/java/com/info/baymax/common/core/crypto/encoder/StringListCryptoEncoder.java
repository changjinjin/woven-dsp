package com.info.baymax.common.core.crypto.encoder;

import com.info.baymax.common.core.crypto.CryptoEncoder;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.utils.ICollections;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认的无处理的数据加密器
 *
 * @param <T> 数据类型
 * @author jingwei.yang
 * @date 2021年3月10日 下午4:59:28
 */
public class StringListCryptoEncoder implements CryptoEncoder<List<String>> {

    @SuppressWarnings("unchecked")
    @Override
    public List<String> encode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType,
                               CryptorDelegater cryptorDelegater) {
        List<String> list = (List<String>) obj;
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> cryptorDelegater.encrypt(t, secretKey, wrapped, cryptoType))
                .collect(Collectors.toList());
        }
        return list;
    }

}
