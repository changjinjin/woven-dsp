package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;

import java.util.List;

public interface RestOperationService extends BaseIdableAndExampleQueryService<String, RestOperation> {

	/**
	 * 初始化接口信息列表
	 * 
	 * @param serviceName 服务名称
	 * @param list        需要初始化的接口列表
	 * @return 处理的条数
	 */
	int initRestOperations(String serviceName, List<RestOperation> list);
}