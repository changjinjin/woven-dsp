package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dss")
@Api(tags = "消费端: 数据源管理接口", value = "数据源管理接口")
public class CustDataSourceController implements BaseEntityController<CustDataSource> {
    @Autowired
    private CustDataSourceService custDataSourceService;

    @Override
    public BaseEntityService<CustDataSource> getBaseEntityService() {
        return custDataSourceService;
    }
}
