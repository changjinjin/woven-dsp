package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.CustApp;
import com.info.baymax.dsp.data.consumer.service.CustAppService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消费端：消费者应用管理", description = "消费者应用管理")
@RestController
@RequestMapping("/cust")
public class CustAppController implements BaseEntityController<CustApp> {

    @Autowired
    private CustAppService custAppService;

    @Override
    public BaseEntityService<CustApp> getBaseEntityService() {
        return custAppService;
    }

}
