package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.CustApp;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.CustAppMapper;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.CustDataSourceMapper;
import com.info.baymax.dsp.data.consumer.service.CustAppService;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class CustDataSourceServiceImpl extends EntityClassServiceImpl<CustDataSource> implements CustDataSourceService {

    @Autowired
    private CustDataSourceMapper custDataSourceMapper;

    @Override
    public MyIdableMapper<CustDataSource> getMyIdableMapper() {
        return custDataSourceMapper;
    }


}
