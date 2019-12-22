package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.FlowDescMapper;
import com.info.baymax.dsp.data.dataset.service.core.FlowDescService;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowDescServiceImpl extends QueryObjectByResourceOrProjectServiceImpl<FlowDesc> implements FlowDescService {

	@Autowired
	private FlowDescMapper flowDescMapper;

	@Override
	public MyIdableMapper<FlowDesc> getMyIdableMapper() {
		return flowDescMapper;
	}

	@Override
	public List<FlowDesc> findByOId(String oid) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("oid", oid)//
				.end());
	}

	@Override
	public List<FlowDesc> findFlowByNotResoure() {
		return flowDescMapper.findFlowByNotResoure();
	}

}
