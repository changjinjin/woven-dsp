package com.info.baymax.dsp.data.dataset.service.core;

import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectService;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceIdService;

import java.util.Date;
import java.util.List;

public interface DatasetService extends BaseMaintableService<Dataset>, QueryObjectByResourceOrProjectService<Dataset>,
    ResourceIdService<Dataset> {

    List<Dataset> findBySchemaIdAndCreateTime(Date start, Date end, String schemaId);

    List<Dataset> findBySchemaId(String schemaId);

    Dataset findOneByName(String tenant, String name);
}
