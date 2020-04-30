package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.sys.entity.security.PermOperationRef;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.PermOperationRefMapper;
import com.info.baymax.dsp.data.sys.service.security.PermOperationRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermOperationRefServiceImpl extends EntityClassServiceImpl<PermOperationRef>
    implements PermOperationRefService {

    @Autowired
    private PermOperationRefMapper permOperationRefMapper;

    @Override
    public MyBaseMapper<PermOperationRef> getMyBaseMapper() {
        return permOperationRefMapper;
    }

}
