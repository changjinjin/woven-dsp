package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.dsp.data.dataset.entity.core.DataSource;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.DataSourceMapper;
import com.info.baymax.dsp.data.dataset.service.core.DataSourceService;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSourceServiceImpl extends QueryObjectByResourceOrProjectServiceImpl<DataSource>
		implements DataSourceService {

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Override
	public MyIdableMapper<DataSource> getMyIdableMapper() {
		return dataSourceMapper;
	}

	@Override
	public List<DataSource> findByType(String type) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo("type", type)));
	}

	@Override
	public List<DataSource> findByType(String tenantId, String type) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder()//
						.andEqualTo(PROPERTY_TENANTID, tenantId)//
						.andEqualTo("type", type)//
				));
	}

	@Override
	public List<String> getDistinctTypes(String tenantId) {
		return dataSourceMapper.getDistinctTypes(tenantId);
	}
}
