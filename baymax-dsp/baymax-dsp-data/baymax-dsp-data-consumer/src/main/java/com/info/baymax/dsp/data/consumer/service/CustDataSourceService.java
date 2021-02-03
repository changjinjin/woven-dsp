package com.info.baymax.dsp.data.consumer.service;

import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;

import java.util.List;

public interface CustDataSourceService extends BaseEntityService<CustDataSource> {

	/**
	 * 查询数据源的数据表名称
	 * 
	 * @param id 数据源ID
	 * @return 表名称集合
	 */
	List<String> selectTableList(String id);

	/**
	 * 数据源连接测试
	 * 
	 * @param dataSource 数据源信息
	 * @return 测试结果
	 */
	boolean jdbcConnect(CustDataSource dataSource);
}
