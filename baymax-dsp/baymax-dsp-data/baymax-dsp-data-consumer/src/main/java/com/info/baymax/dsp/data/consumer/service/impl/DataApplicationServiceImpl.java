package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.DataApplicationMapper;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:26
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DataApplicationServiceImpl extends EntityClassServiceImpl<DataApplication>
    implements DataApplicationService {

    @Autowired
    private DataApplicationMapper dataApplicationMapper;

    @Override
    public MyIdableMapper<DataApplication> getMyIdableMapper() {
        return dataApplicationMapper;
    }

    @Override
    public DataApplication save(DataApplication t) {
        String name = t.getName();
        DataApplication record = new DataApplication();
        record.setTenantId(SaasContext.getCurrentTenantId());
        record.setName(name);
        if (exists(record)) {
            t.setName(name + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
        }
        return DataApplicationService.super.save(t);
    }

    @Override
    public void updateDataApplicationStatus(Long id, Integer status) {
        DataApplication record = new DataApplication();
        record.setId(id);
        record.setStatus(status);
        dataApplicationMapper.updateByPrimaryKeySelective(record);
    }

}
