package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.DataApplicationMapper;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:26
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DataApplicationServiceImpl extends EntityClassServiceImpl<DataApplication> implements DataApplicationService {

    @Autowired
    DataApplicationMapper dataApplicationMapper;

    @Override
    public MyIdableMapper<DataApplication> getMyIdableMapper() {
        return dataApplicationMapper;
    }

    @Override
    public Long createDataApplication(DataApplication dataApplication) {
        long id = dataApplicationMapper.insert(dataApplication);
        return id;
    }

    @Override
    public void updateDataApplication(DataApplication dataApplication) {
        dataApplicationMapper.updateByPrimaryKey(dataApplication);
    }

    @Override
    public void deleteByDataResIds(Long tenantId, List<Long> dataResIds){
        dataApplicationMapper.deleteByDataResIds(tenantId, dataResIds);
    }
}
