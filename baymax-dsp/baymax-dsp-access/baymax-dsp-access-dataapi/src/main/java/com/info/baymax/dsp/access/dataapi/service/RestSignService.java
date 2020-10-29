package com.info.baymax.dsp.access.dataapi.service;

/**
 * rest 接口sign相关接口
 *
 * @author jingwei.yang
 * @date 2020年4月20日 上午10:09:26
 */
public interface RestSignService {

    /**
     * 根据消费者应用的accessKey获取一个临时的接口请求的singKey
     *
     * @param accessKey 消费者应用的accessKey
     * @return 生成的接口请求的signKey，并保存一定的有效期，过期则失效
     */
    String secertkey(String accessKey);

    /**
     * 根据accessKey从缓存中拿生成的秘钥信息
     *
     * @param accessKey 消费端应用accessKey
     * @return 秘钥信息，如果不存在则提示秘钥过期
     */
    String signKeyIfExist(String accessKey);

}
