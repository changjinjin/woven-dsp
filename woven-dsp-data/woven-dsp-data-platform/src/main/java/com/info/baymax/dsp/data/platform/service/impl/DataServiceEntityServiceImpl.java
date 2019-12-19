package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.platform.entity.DataService;
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
public class DataServiceEntityServiceImpl extends EntityClassServiceImpl<DataService> implements DataServiceEntityService {

    @Autowired
    DataServiceMapper dataServiceMapper;

    @Override
    public MyIdableMapper<DataService> getMyIdableMapper() {
        return dataServiceMapper;
    }

    @Override
    public List<DataService> querySpecialDataService(Integer type, Integer status, Integer isRunning) {
        return dataServiceMapper.querySpecialDataService(type,status,isRunning);
    }

    @Override
    public void updateDataServiceToRunning(Integer type, Integer status, Integer isRunning){
        dataServiceMapper.updateDataServiceToRunning(type,status,isRunning);
    }

    @Override
    public void updateDataServiceToRunning(Long id) {
        dataServiceMapper.updateDataServiceToRunning(id);
    }


    @Override
    public void recoverDataService() {
        dataServiceMapper.recoverDataService();
    }

    @Override
    public void updateFinishedDataService() {
        dataServiceMapper.updateFinishedDataService();
    }
}
