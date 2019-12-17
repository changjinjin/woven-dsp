package com.info.baymax.dsp.data.dataset.service.core;

import java.util.Date;
import java.util.List;

import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.BaseMaintableService;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectService;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceIdService;

public interface DatasetService extends BaseMaintableService<Dataset>, QueryObjectByResourceOrProjectService<Dataset>,
    ResourceIdService<Dataset> {

    List<Dataset> findBySchemaIdAndCreateTime(Date start, Date end, String schemaId);

    List<Dataset> findBySchemaId(String schemaId);
}
