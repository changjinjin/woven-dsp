package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消费端：消费者应用配置管理", description = "消费者应用配置管理")
@RestController
@RequestMapping("/custApp")
public class DataCustAppController implements BaseEntityController<DataCustApp> {

    @Autowired
    private DataCustAppService dataCustAppService;

    @Override
    public BaseEntityService<DataCustApp> getBaseEntityService() {
        return dataCustAppService;
    }

    @Override
    public Response<IPage<DataCustApp>> page(ExampleQuery query) {
        query = ExampleQuery.builder(query);
        query.fieldGroup().andEqualTo("owner", SaasContext.getCurrentUserId());
        return BaseEntityController.super.page(query);
    }

}
