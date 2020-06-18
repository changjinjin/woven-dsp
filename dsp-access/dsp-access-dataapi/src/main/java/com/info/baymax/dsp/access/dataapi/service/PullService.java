package com.info.baymax.dsp.access.dataapi.service;

import com.info.baymax.common.page.IPage;

import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
public interface PullService {

	IPage<MapEntity> query(String storage, Map<String, String> fieldMap, Map<String, String> conf, String[] includes,
			int offset, int limit);

}
