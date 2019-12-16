package com.info.baymax.dsp.data.consumer.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.Consumer;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.ConsumerMapper;
import com.info.baymax.dsp.data.consumer.service.ConsumerService;

@Service
@Transactional(rollbackOn = Exception.class)
public class ConsumerServiceImpl extends EntityClassServiceImpl<Consumer> implements ConsumerService {

    @Autowired
    private ConsumerMapper consumerMapper;

    @Override
    public MyIdableMapper<Consumer> getMyIdableMapper() {
        return consumerMapper;
    }

}
