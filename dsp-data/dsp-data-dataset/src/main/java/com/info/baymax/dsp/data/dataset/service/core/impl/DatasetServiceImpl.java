package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.entity.core.Schema;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.DatasetMapper;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.SchemaMapper;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatasetServiceImpl extends QueryObjectByResourceOrProjectServiceImpl<Dataset> implements DatasetService {

	@Autowired
	private DatasetMapper datasetMapper;
	@Autowired
	private SchemaMapper schemaMapper;

	@Override
	public MyIdableMapper<Dataset> getMyIdableMapper() {
		return datasetMapper;
	}

	@Override
	public List<Dataset> findBySchemaIdAndCreateTime(Date start, Date end, String schemaId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andBetween("createTime", start, end) //
				.andEqualTo("schemaId", schemaId)//
				.end());
	}

	@Override
	public List<Dataset> findBySchemaId(String schemaId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("schemaId", schemaId)//
				.end());
	}

	@Override
	public Page<Dataset> findObjectPage(ExampleQuery query) {
		return queryPageWithSchemas(super.findObjectPage(query));
	}

	@Override
	public Page<Dataset> getInstanceCollectionByResDirAndCondition(String userId, QueryObject queryObject) {
		return queryPageWithSchemas(super.getInstanceCollectionByResDirAndCondition(userId, queryObject));
	}

	@Override
	public List<Dataset> findList(QueryObject queryObject) {
		return querySchemas(super.findList(queryObject));
	}

	@Override
	public Dataset getSingleResult(QueryObject queryObject) {
		return querySchema(super.getSingleResult(queryObject));
	}

	private Page<Dataset> queryPageWithSchemas(Page<Dataset> page) {
		if (page == null) {
			return page;
		}
		page.setContent(querySchemas(page.getContent()));
		return page;
	}

	private List<Dataset> querySchemas(List<Dataset> datasets) {
		if (ICollections.hasElements(datasets)) {
			List<String> schemaIds = datasets.stream().map(t -> t.getSchemaId()).collect(Collectors.toList());
			//如果没有查询schemaId字段，则不必要查询直接返回即可
			if (ICollections.hasNoElements(schemaIds)) {
				return datasets;
			}

			List<Schema> schemas = schemaMapper.selectByPrimaryKeys(schemaIds);
			if (ICollections.hasElements(schemas)) {
				for (Dataset dataset : datasets) {
					for (Schema schema : schemas) {
						if (schema.getId().equals(dataset.getSchemaId())) {
							dataset.setSchema(schema);
						}
					}
				}
			}
		}
		return datasets;
	}

	private Dataset querySchema(Dataset dataset) {
		if (dataset == null) {
			return dataset;
		}
		String schemaId = dataset.getSchemaId();
		if (dataset != null && schemaId != null && dataset.getSchema() == null) {
			Schema schema = schemaMapper.selectByPrimaryKey(schemaId);
			if (schema != null) {
				dataset.setSchema(schema);
			}
		}
		return dataset;
	}

	@Override
	public Dataset findOneByName(String tenant, String name){
		List<Dataset> list = selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("tenantId", tenant)
				.andEqualTo("name", name)
				.end()//
				.orderByDesc("lastModifiedTime")
		);

		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

}
