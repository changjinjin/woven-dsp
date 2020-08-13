package com.info.baymax.dsp.data.dataset.service.resource;

import com.info.baymax.common.jpa.criteria.query.FieldObject;
import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper;
import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.jpa.criteria.query.SortObject;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.JoinSql;
import com.info.baymax.common.queryapi.field.FieldGroup;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceType;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

@Slf4j
public abstract class QueryObjectByResourceOrProjectServiceImpl<T extends ResourceId> extends EntityClassServiceImpl<T>
		implements QueryObjectByResourceOrProjectService<T> {

	private String pathIsNullResourceId = "";

	@Autowired
	private ResourceDescService resourceDescService;

	@Override
	public ExampleQuery getInstanceCollectionByResDirAndConditionDynamicTable(String userId, QueryObject queryObject) {
		String tableName = getTableName();
		if (StringUtils.isEmpty(tableName)) {
			log.warn("获取实体类型{}表名称失败。", getEntityClass());
			return null;
		}

		List<FieldObject> fieldList = queryObject.getFieldList();

		String[] resourceIds = null;
		// 处理查询条件，"tenant"、"parentId"、"resourceId" 需要单独处理
		if (fieldList != null && fieldList.size() > 0) {
			Iterator<FieldObject> ite = fieldList.iterator();
			while (ite.hasNext()) {
				FieldObject next = ite.next();
				Object fieldValue = next.getFieldValue();
				if (fieldValue == null) {
					continue;
				}
				if ("tenant".equals(next.getFieldName())) {
					String tenantId = ((Tenant) fieldValue).getId();
					if (StringUtils.isEmpty(tenantId)) {
						continue;
					}
					next.setFieldName("tenantId");
					next.setFieldValue(tenantId);
				} else if ("parentId".equals(next.getFieldName()) || "resourceId".equals(next.getFieldName())) {
					String path = getPath(fieldValue.toString(), new StringBuilder(), true);
					// 查询要筛选的目录列表
					resourceIds = resourceDescService.selectUserResourceIdsByResTypeAndPath(userId,
							ResourceType.getByTable(tableName).name(), path);
					if (resourceIds == null || resourceIds.length == 0) {
						return null;
					}
					ite.remove();// 删除该条件
				}
			}
		}

		// 构建query条件
		return exampleQuery(queryObject).fieldGroup(FieldGroup.builder().andIn("resourceId", resourceIds));
	}

	/**
	 * 处理掉一些分页不必要查询的字段以提高效率，尤其是一些大字段需要转化的字段，如：dataset中的storageConfigurations、formatConfigurations等。
	 * 
	 * @param tableName 查询的表名
	 * @param query     trim之前的query
	 * @return trim后的query
	 */
	@SuppressWarnings("unused")
	private ExampleQuery trimQueryFields(String tableName, ExampleQuery query) {
		switch (ResourceType.getByTable(tableName)) {
		case dataset_dir:
			query.excludeProperties("storageConfigurations", "formatConfigurations", "description");
			break;
		case schema_dir:
			query.excludeProperties("primaryKeys", "description");
			break;
		case datasource_dir:
			query.excludeProperties("description");
			break;
		case flow_dir:
			query.excludeProperties("steps", "links", "parameters", "inputs", "outputs", "dependencies");
			break;
		case standard_dir:
			query.excludeProperties("attributes", "description");
			break;
		default:
			query.excludeProperties("description");
			break;
		}
		return query;
	}

	public String getPath(String id, StringBuilder path, boolean isFirst) {
		if (StringUtils.isEmpty(id)) {
			return path.append(";").toString();
		}

		ResourceDesc resourceDesc = resourceDescService.selectByPrimaryKey(id);
		String pathStr = null;
		if (resourceDesc != null) {
			pathStr = resourceDesc.getPath();
		}

		if (!StringUtils.isEmpty(pathStr) && isFirst) {
			return pathStr;
		} else {
			return getPathRecursion(id, path, isFirst);
		}
	}

	public String getPathRecursion(String id, StringBuilder path, boolean isFirst) {
		if (isFirst) {
			pathIsNullResourceId = id;
		}
		ResourceDesc resourceDesc = resourceDescService.selectByPrimaryKey(id);
		String pathStr = null;
		if (resourceDesc != null) {
			pathStr = resourceDesc.getPath();
		}
		if (!StringUtils.isEmpty(pathStr) && isFirst) {
			return pathStr;
		} else {
			isFirst = false;
			if (resourceDesc == null) {
				return "";
			}
			if (resourceDesc.getParentId() != null) {
				path.insert(0, resourceDesc.getName()).insert(0, ";");
				getPathRecursion(resourceDesc.getParentId(), path, isFirst);
			} else {
				path.insert(0, resourceDesc.getName());
			}
			ResourceDesc pathIsNullResource = resourceDescService.selectByPrimaryKey(pathIsNullResourceId);
			if (pathIsNullResource != null) {
				pathIsNullResource.setPath(path.append(";").toString());
				resourceDescService.updateByPrimaryKey(pathIsNullResource);
			}
			return path.append(";").toString();
		}
	}

	@Override
	public ExampleQuery getInstancesByResourcePathDynamicTable(QueryObject queryObject, String path) {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("INNER JOIN merce_resource_dir rs ON t.resource_id = rs.id and rs.path like '").append(path)
				.append("%'");
		return exampleQuery(queryObject)
				.joinSql(JoinSql.builder().appendTable(sqlBuf.toString()).tableAlias("t").build());
	}

	@Override
	public ExampleQuery getInstanceIdsByResourcePathDynamicTable(QueryObject queryObject, String path) {
		return getInstancesByResourcePathDynamicTable(queryObject, path).selectProperties("id");
	}

	@Override
	public ExampleQuery getInstanceCollectionByProjectDirAndConditionDynamicTable(QueryObject queryObject) {
		String tableName = getTableName();
		if (StringUtils.isEmpty(tableName)) {
			log.warn("获取实体类型{}表名称失败。", getEntityClass());
			return null;
		}

		// 处理查询条件
		List<FieldObject> fieldList = queryObject.getFieldList();
		StringBuilder fieldBuf = new StringBuilder();
		if (ICollections.hasElements(fieldList)) {
			Iterator<FieldObject> ite = fieldList.iterator();
			while (ite.hasNext()) {
				FieldObject next = ite.next();
				JpaCriteriaHelper.LogicalOperator operator = next.getLogicalOperator();
				String fieldName = next.getFieldName();
				Object fieldValue = next.getFieldValue();
				if (fieldValue == null) {
					return null;
				}
				if ("tenant".equals(fieldName)) {
					String tenantId = ((Tenant) fieldValue).getId();
					if (StringUtils.isEmpty(tenantId)) {
						continue;
					}
					next.setFieldName("tenantId");
					next.setFieldValue(tenantId);
				} else if ("projectId".equals(fieldName) || "resourceId".equals(fieldName)) {
					fieldBuf.append(" ").append(operator).append(" ").append("r.project_id").append("= '")
							.append(fieldValue.toString()).append("'");
					ite.remove();
				} else if ("instanceType".equals(fieldName)) {
					fieldBuf.append(" ").append(operator).append(" ").append("r.instance_type").append("= '")
							.append(fieldValue).append("'");
					ite.remove();
				}
			}
		}

		// 处理排序条件
		SortObject sortObject = queryObject.getSortObject();
		StringBuilder sortBuf = new StringBuilder();
		if (sortObject != null) {
			String field = sortObject.getField();
			if (!StringUtils.isEmpty(field) && field.equals("lastModifiedTime")) {
				sortBuf.append(" order by r.last_modified_time ").append(sortObject.getOrderDirection());
				queryObject.setSortObject(null);
			}
		}

		// 处理动态子查询语句
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("(").append("SELECT t.* FROM ").append(tableName).append(
				" t INNER JOIN merce_data_project_ref r ON t.id = r.instance_id WHERE r.last_modified_time is not null")
				.append(fieldBuf).append(sortBuf).append(") tt");
		// 构建query条件
		return exampleQuery(queryObject).joinSql(JoinSql.builder().dynamicTable(sqlBuf.toString()).build());
	}
}
