package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haijun
 * @Date: 2019/12/16 11:25 http://xxx.yyy/api/dsp/consumer/
 */
@Api(tags = "消费者针对数据资源的操作接口", description = "消费者数据资源操作接口")
@RestController
@RequestMapping("/application")
public class CustDataApplicationController implements BaseEntityController<DataApplication> {

    @Autowired
    private DataApplicationService dataApplicationService;

    @Override
    public BaseEntityService<DataApplication> getBaseEntityService() {
        return dataApplicationService;
    }

}
