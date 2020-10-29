package com.info.baymax.dsp.access.dataapi.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.info.baymax.common.queryapi.exception.ServiceException;
import com.info.baymax.common.queryapi.result.ErrType;
import com.info.baymax.common.utils.PasswordGenerator;
import com.info.baymax.common.utils.crypto.RSAGenerater;
import com.info.baymax.dsp.access.dataapi.service.RestSignService;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RestSignServiceImpl implements RestSignService {
    private String prefix = "sign_";
    private static final Cache<String, String> ACCESSKEY_CACHE = CacheBuilder.newBuilder().maximumSize(1000) // 设置缓存的最大容量
        .expireAfterWrite(20, TimeUnit.MINUTES) // 设置缓存在写入10分钟后失效
        .concurrencyLevel(10) // 设置并发级别为10
        .recordStats() // 开启缓存统计
        .build();

    @Autowired
    private DataCustAppService dataCustAppService;

    @Override
    public String secertkey(String accessKey) {
        DataCustApp app = dataCustAppService.selectByAccessKeyNotNull(accessKey);
        // 生成随机的对称加密秘钥并使用app的私钥进行加密返回给用户
        String generateRandomPassword = new PasswordGenerator(16, 3).generateRandomPassword();
        String encryptKey = RSAGenerater.encryptByPrivateKey(generateRandomPassword, app.getPrivateKey());
        ACCESSKEY_CACHE.put(signKey(accessKey), encryptKey);
        return encryptKey;
    }

    @Override
    public String signKeyIfExist(String accessKey) {
        DataCustApp app = dataCustAppService.selectByAccessKeyNotNull(accessKey);
        // 返回解密的对称加密秘钥明文用于加解密报文内容
        String wrappKey = ACCESSKEY_CACHE.getIfPresent(signKey(accessKey));
        if (StringUtils.isNotEmpty(wrappKey)) {
            return RSAGenerater.decryptByPublicKey(wrappKey, app.getPublicKey());
        }
        throw new ServiceException(ErrType.SECRET_KEY_ERROR,
            "There is no secret key available in the current request. The secret key has expired or has been deleted. Please request a secret key first.");
    }

    private String signKey(String accessKey) {
        return prefix + accessKey;
    }
}
