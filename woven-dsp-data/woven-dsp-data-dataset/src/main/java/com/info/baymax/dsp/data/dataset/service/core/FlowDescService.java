package com.info.baymax.dsp.data.dataset.service.core;


import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectService;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceIdService;

import java.util.List;

public interface FlowDescService extends BaseMaintableService<FlowDesc>,
        QueryObjectByResourceOrProjectService<FlowDesc>, ResourceIdService<FlowDesc> {

    List<FlowDesc> findByOId(String oid);

    List<FlowDesc> findFlowByNotResoure();
}
