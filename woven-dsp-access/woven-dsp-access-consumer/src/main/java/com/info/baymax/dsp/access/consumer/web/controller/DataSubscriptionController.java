package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.DataSubscription;
import com.info.baymax.dsp.data.consumer.service.DataSubscriptionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消费端：消费者数据订阅", description = "消费者数据订阅相关接口")
@RestController
@RequestMapping("/subscribe")
@Deprecated
public class DataSubscriptionController implements BaseEntityController<DataSubscription> {

    @Autowired
    private DataSubscriptionService dataSubscriptionService;

    @Override
    public BaseEntityService<DataSubscription> getBaseEntityService() {
        return dataSubscriptionService;
    }
}
