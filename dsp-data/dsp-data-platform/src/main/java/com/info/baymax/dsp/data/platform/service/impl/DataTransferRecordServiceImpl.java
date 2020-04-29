package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.platform.entity.DataTransferRecord;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataTransferRecordMapper;
import com.info.baymax.dsp.data.platform.service.DataTransferRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class DataTransferRecordServiceImpl extends EntityClassServiceImpl<DataTransferRecord>
    implements DataTransferRecordService {

    @Autowired
    private DataTransferRecordMapper dataTransferRecordMapper;

    @Override
    public MyIdableMapper<DataTransferRecord> getMyIdableMapper() {
        return dataTransferRecordMapper;
    }

}
