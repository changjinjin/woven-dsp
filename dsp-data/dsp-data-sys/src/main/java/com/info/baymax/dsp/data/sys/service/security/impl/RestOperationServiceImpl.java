package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RestOperationMapper;
import com.info.baymax.dsp.data.sys.service.security.RestOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestOperationServiceImpl extends EntityClassServiceImpl<RestOperation> implements RestOperationService {
    @Autowired
    private RestOperationMapper restOperationMapper;

    @Override
    public MyIdableMapper<RestOperation> getMyIdableMapper() {
        return restOperationMapper;
    }

}
