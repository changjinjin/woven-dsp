package com.info.baymax.dsp.access.dataapi.service.impl;

import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.dsp.access.dataapi.service.ElasticSearchService;
import com.info.baymax.dsp.access.dataapi.service.Engine;
import com.info.baymax.dsp.access.dataapi.service.MapEntity;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
@Service
@Slf4j
public class PullServiceImpl implements PullService {

    @Autowired
    ElasticSearchService elasticSearchService;

    @Override
    public IPage<MapEntity> query(String storage, Map<String, String> fieldMap, Map<String, String> conf,
                                  String[] includes, int offset, int limit) {
        switch (Engine.valueOf(storage.toUpperCase())) {
            case ELASTICSEARCH:
                return elasticSearchService.query(conf, includes, IPageable.offset(offset, limit));
            default:
                log.info("{} storage doesn't support PULL type", storage);
                return null;
        }
    }
}
