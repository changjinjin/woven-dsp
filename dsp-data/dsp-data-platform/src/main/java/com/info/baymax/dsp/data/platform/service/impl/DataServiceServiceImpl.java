package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.field.FieldGroup;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataServiceMapper;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/18
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DataServiceServiceImpl extends EntityClassServiceImpl<DataService> implements DataServiceService {

    @Autowired
    private DataServiceMapper dataServiceMapper;

    @Override
    public MyIdableMapper<DataService> getMyIdableMapper() {
        return dataServiceMapper;
    }

    @Override
    public DataService saveOrUpdate(DataService t) {
        List<FieldMapping> fieldMappings = t.getFieldMappings();
        if (ICollections.hasElements(fieldMappings)) {
            t.setFieldMappings(fieldMappings.stream().filter(f -> StringUtils.isNotEmpty(f.getTargetField()))
                .collect(Collectors.toList()));
        }
        return DataServiceService.super.saveOrUpdate(t);
    }

    @Override
    public List<DataService> querySpecialDataService(Integer[] type, Integer[] status, Integer[] isRunning) {
        return selectList(ExampleQuery.builder(DataService.class).forUpdate(true).fieldGroup(
            FieldGroup.builder().andIn("type", type).andIn("status", status).andIn("isRunning", isRunning)));
    }

    @Override
    public void updateDataServiceRunningStatus(Long id, Integer isRunning) {
        DataService record = new DataService();
        record.setIsRunning(isRunning);
        record.setId(id);
        updateByPrimaryKeySelective(record);
    }

    @Override
    public void recoverDataService() {
        DataService record = new DataService();
        record.setIsRunning(0);
        updateByExampleSelective(record, ExampleQuery.builder().fieldGroup(FieldGroup.builder().andEqualTo("status", 1)
            .andGroup(FieldGroup.builder().andEqualTo("isRunning", 1).orEqualTo("scheduleType", "cron"))));
    }

    @Override
    public void updateStatusByApplicationId(Long applicationId, Integer status, Integer isRunning) {
        DataService record = new DataService();
        record.setStatus(status);
        record.setIsRunning(isRunning);
        updateByExampleSelective(record,
            ExampleQuery.builder().fieldGroup(FieldGroup.builder().andEqualTo("applicationId", applicationId)));
    }
}
