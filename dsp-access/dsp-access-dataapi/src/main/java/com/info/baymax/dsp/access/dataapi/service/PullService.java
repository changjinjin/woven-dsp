package com.info.baymax.dsp.access.dataapi.service;

import com.info.baymax.common.page.IPage;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.condition.RequestQuery;

import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
public interface PullService {

    /**
     * 分页查询数据
     *
     * @param storage  存储类型
     * @param fieldMap 字段映射
     * @param conf     配置参数
     * @param query    查询参数
     * @return 结果集
     */
    IPage<MapEntity> query(String storage, Map<String, String> fieldMap, Map<String, String> conf, RequestQuery query);

}
