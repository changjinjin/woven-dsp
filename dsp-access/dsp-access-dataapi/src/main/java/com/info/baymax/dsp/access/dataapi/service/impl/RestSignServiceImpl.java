package com.info.baymax.dsp.access.dataapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.utils.PasswordGenerator;
import com.info.baymax.common.utils.crypto.RSAGenerater;
import com.info.baymax.dsp.access.dataapi.service.RestSignService;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.sys.constant.CacheNames;

@Service
public class RestSignServiceImpl implements RestSignService {

	private String prefix = "sign_";

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private DataCustAppService dataCustAppService;

	@Cacheable(cacheNames = CacheNames.CACHE_SIGN, key = "'sign_'+#accessKey", unless = "#result == null")
	@Override
	public String secertkey(String accessKey) {
		DataCustApp app = dataCustAppService.selectByAccessKeyNotNull(accessKey);
		// 生成随机的对称加密秘钥并使用app的私钥进行加密返回给用户
		return RSAGenerater.encryptByPrivateKey(new PasswordGenerator(16, 3).generateRandomPassword(),
				app.getPrivateKey());
	}

	@Override
	public String signKeyIfExist(String accessKey) {
		DataCustApp app = dataCustAppService.selectByAccessKeyNotNull(accessKey);
		// 返回解密的对称加密秘钥明文用于加解密报文内容
		Cache cache = cacheManager.getCache(CacheNames.CACHE_SIGN);
		if (cache != null) {
			ValueWrapper valueWrapper = cache.get(prefix + accessKey);
			if (valueWrapper != null) {
				return RSAGenerater.decryptByPublicKey(valueWrapper.get().toString(), app.getPublicKey());
			}
		}
		throw new ServiceException(ErrType.SECRET_KEY_ERROR);
	}
}
