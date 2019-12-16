package com.info.baymax.dsp.data.consumer.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.Customer;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.CustomerMapper;
import com.info.baymax.dsp.data.consumer.service.CustomerService;

@Service
@Transactional(rollbackOn = Exception.class)
public class CustomerServiceImpl extends EntityClassServiceImpl<Customer> implements CustomerService {

    @Autowired
    private CustomerMapper consumerMapper;

    @Override
    public MyIdableMapper<Customer> getMyIdableMapper() {
        return consumerMapper;
    }

}
