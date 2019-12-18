package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataServiceEntity;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataServiceMapper;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/18
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DataServiceEntityServiceImpl extends EntityClassServiceImpl<DataServiceEntity> implements DataServiceEntityService {

    @Autowired
    DataServiceMapper dataServiceMapper;

    @Override
    public Integer createDataService(DataResource dataResource) {
        return null;
    }

    @Override
    public void updateDataService(DataResource dataResource) {

    }

    @Override
    public void deleteDataService(List<Long> ids) {

    }

    @Override
    public MyIdableMapper<DataServiceEntity> getMyIdableMapper() {
        return dataServiceMapper;
    }
}
