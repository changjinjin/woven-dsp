package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustDataSourceController implements BaseEntityController<CustDataSource> {
    @Autowired
    private CustDataSourceService custDataSourceService;

    @Override
    public BaseEntityService<CustDataSource> getBaseEntityService() {
        return custDataSourceService;
    }

}
