package com.info.baymax.dsp.data.dataset.service.core;

import com.info.baymax.common.persistence.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.dataset.entity.core.Schema;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectService;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceIdService;

import java.util.List;

public interface SchemaService
    extends BaseMaintableService<Schema>, QueryObjectByResourceOrProjectService<Schema>, ResourceIdService<Schema> {

    List<Schema> findSchemaByNotResoure();

    List<Schema> findSchemaByNameAndPath(String tenant, String owner, String name, String path);

    List<Schema> findSchemaByOId(String tenant,String oid);

    List<Schema> findSchemaByOIdAndVersion(String tenant, String oid, Integer version);

    Schema findOneByName(String tenant, String name);
}
