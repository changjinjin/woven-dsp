package com.info.baymax.dsp.access.dataapi.service;

import com.info.baymax.access.dataapi.api.AggRequest;
import com.info.baymax.access.dataapi.api.MapEntity;
import com.info.baymax.access.dataapi.api.RecordRequest;
import com.info.baymax.common.queryapi.page.IPage;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
public interface PullService {

    /**
     * 数据拉取请求查询逻辑
     *
     * @param request 拉取请求参数
     * @param hosts   配置主机
     * @return 数据结果集
     */
    IPage<MapEntity> pullRecords(RecordRequest request, String hosts);

    /**
     * 数据拉取请求查询逻辑SQL预览
     *
     * @param request 拉取请求参数
     * @param hosts   配置主机
     * @return SQL预览
     */
    String pullRecordsSql(RecordRequest request, String hosts);

    /**
     * 数据聚合请求查询逻辑
     *
     * @param request 拉取请求参数
     * @param hosts   配置主机
     * @return 聚合结果集
     */
    IPage<MapEntity> pullAggs(AggRequest request, String hosts);

    /**
     * 数据聚合请求查询逻辑SQL预览
     *
     * @param request 拉取请求参数
     * @param hosts   配置主机
     * @return SQL预览
     */
    String pullAggsSql(AggRequest request, String hosts);

}
