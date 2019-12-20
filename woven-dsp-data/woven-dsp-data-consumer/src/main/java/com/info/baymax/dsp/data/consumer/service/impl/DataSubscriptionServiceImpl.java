package com.info.baymax.dsp.data.consumer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.DataSubscription;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.DataSubscriptionMapper;
import com.info.baymax.dsp.data.consumer.service.DataSubscriptionService;

@Service
public class DataSubscriptionServiceImpl extends EntityClassServiceImpl<DataSubscription>
    implements DataSubscriptionService {

    @Autowired
    private DataSubscriptionMapper dataSubscriptionMapper;

    @Override
    public MyIdableMapper<DataSubscription> getMyIdableMapper() {
        return dataSubscriptionMapper;
    }
}
