package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.platform.entity.DataPolicy;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataPolicyMapper;
import com.info.baymax.dsp.data.platform.service.DataPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class DataPolicyServiceImpl extends EntityClassServiceImpl<DataPolicy> implements DataPolicyService {

    @Autowired
    private DataPolicyMapper dataPolicyMapper;

    @Override
    public MyIdableMapper<DataPolicy> getMyIdableMapper() {
        return dataPolicyMapper;
    }


}
