package com.info.baymax.dsp.data.dataset.service.core;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.MapEntity;
import com.info.baymax.common.persistence.entity.base.BaseMaintableService;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.dsp.data.dataset.entity.core.DataSource;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectService;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceIdService;

import java.util.List;

public interface DataSourceService extends BaseMaintableService<DataSource>,
    QueryObjectByResourceOrProjectService<DataSource>, ResourceIdService<DataSource> {

    List<DataSource> findByType(String type);

    List<DataSource> findByType(String tenantId, String type);

    List<String> getDistinctTypes(String tenantId);

    List<String> fetchTableList(String dataSourceId);

    List<String> fetchTableColumns(String dataSourceId, String table);

    IPage<MapEntity> previewBySql(String dataSourceId, SqlQuery query);

    DataSource findOneByName(String tenant, String name);
}
