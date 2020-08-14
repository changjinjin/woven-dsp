package com.info.baymax.access.dataapi.client;

import com.info.baymax.access.dataapi.api.MapEntity;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;

public interface PullClient {

    /**
     * 根据accessKey获取报文解析秘钥
     *
     * @param accessKey 接入Key
     * @param publicKey 报文解密公钥
     * @return 秘钥信息
     */
    String secertkey(String accessKey, String publicKey) throws PullClientException;

    /**
     * 数据记录请求接口
     *
     * @param accessKey     接入Key
     * @param publicKey     报文解密公钥
     * @param dataServiceId 服务ID
     * @param encrypted     是否加密
     * @param query         记录查询条件
     * @param hosts         绑定的host信息
     * @return 纪录数据
     * @throws PullClientException
     */
    IPage<MapEntity> pullRecords(String accessKey, String publicKey, Long dataServiceId, boolean encrypted,
                                 String hosts, RecordQuery query) throws PullClientException;

    /**
     * 数据记录请求接口
     *
     * @param accessKey     接入Key
     * @param publicKey     报文解密公钥
     * @param dataServiceId 服务ID
     * @param encrypted     是否加密
     * @param query         聚合查询条件
     * @param hosts         绑定的host信息
     * @return 聚合数据
     * @throws PullClientException
     */
    IPage<MapEntity> pullAggs(String accessKey, String publicKey, Long dataServiceId, boolean encrypted, String hosts,
                              AggQuery query) throws PullClientException;

}
