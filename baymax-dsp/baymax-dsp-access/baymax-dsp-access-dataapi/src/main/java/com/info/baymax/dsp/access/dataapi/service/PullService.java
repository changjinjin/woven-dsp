package com.info.baymax.dsp.access.dataapi.service;

import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.result.MapEntity;
import com.info.baymax.dsp.access.dataapi.api.AggRequest;
import com.info.baymax.dsp.access.dataapi.api.RecordRequest;
import com.info.baymax.dsp.access.dataapi.api.SqlRequest;

import javax.validation.Valid;

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

    /**
     * 根据sql模板查询
     *
     * @param request sql模板查询信息
     * @param hosts   配置主机
     * @return 查询结果
     */
    IPage<MapEntity> pullBySql(@Valid SqlRequest request, String hosts);

}
