package com.info.baymax.dsp.access.dataapi.service;

import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;

import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/20
 */
public interface ElasticSearchService {

	IPage<MapEntity> query(Map<String, String> conf, String[] includes, IPageable pageable);

}
