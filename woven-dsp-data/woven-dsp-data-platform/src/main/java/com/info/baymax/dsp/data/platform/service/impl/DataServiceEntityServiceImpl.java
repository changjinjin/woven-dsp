package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleHelper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
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
        //select * from dsp_data_service where (schedule_type = 'once' or schedule_type='cron') and type = #{type, jdbcType=INTEGER} and status = #{status, jdbcType=INTEGER} and is_running = #{isRunning, jdbcType=INTEGER} for update")
        ExampleQuery query = ExampleQuery.builder(DataService.class)//
                .fieldGroup()
                .andIn("scheduleType", new String[] { "cron", "once"})//
                .andEqualTo("type", type)
                .andEqualTo("status", status)
                .andEqualTo("isRunning", isRunning)
                .end();
        List<DataService> list = selectByExample(ExampleHelper.createExample(query,getEntityClass()));
        return list;
    }

    @Override
    public void updateDataServiceRunningStatus(Long id, Integer isRunning) {
        dataServiceMapper.updateDataServiceRunningStatus(id, isRunning);
    }

    @Override
    public void restoreDataServiceRunningStatus(Long id){
        dataServiceMapper.updateDataServiceRunningStatus(id, 0);
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
