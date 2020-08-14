package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.dataset.entity.core.FlowHistDesc;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.FlowHistDescMapper;
import com.info.baymax.dsp.data.dataset.service.core.FlowHistDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowHistDescServiceImpl extends EntityClassServiceImpl<FlowHistDesc> implements FlowHistDescService {

	@Autowired
	private FlowHistDescMapper flowHistDescMapper;

	@Override
	public MyIdableMapper<FlowHistDesc> getMyIdableMapper() {
		return flowHistDescMapper;
	}

	@Override
	public List<FlowHistDesc> findByOId(String oid) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo("oid", oid))//
		);
	}

	@Override
	public FlowHistDesc findByOIdAndVersion(String oid, int version) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo("oid", oid).andEqualTo("version", version))//
		);
	}

	@Override
	public int deleteByOId(String tenantId, String owner, String oid) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)
						.andEqualTo(PROPERTY_OWNER, owner).andEqualTo("oid", oid))//
		);
	}

	@Override
	public int deleteByOIds(String tenantId, String owner, String[] oids) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)
						.andEqualTo(PROPERTY_OWNER, owner).andIn("oid", oids))//
		);
	}

	@Override
	public Integer findMaxVersionByFlowId(String flowId) {
		return flowHistDescMapper.findMaxVersionByFlowId(flowId);
	}

	@Override
	public FlowHistDesc findAllByOwnerIdAndNameAndVersion(String owner, Integer version, String name) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_OWNER, owner).andEqualTo("version", version)
						.andEqualTo("name", name))//
		);
	}

	@Override
	public FlowHistDesc findAllByNameAndVersion(Integer version, String name) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo("version", version).andEqualTo("name", name))//
		);
	}

}
