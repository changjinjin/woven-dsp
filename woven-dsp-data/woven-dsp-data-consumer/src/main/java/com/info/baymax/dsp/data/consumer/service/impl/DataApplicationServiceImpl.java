package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.DataApplicationMapper;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:26
 */
@Service
public class DataApplicationServiceImpl implements DataApplicationService {

    private static final long serialVersionUID = 7694363257932976646L;

    @Autowired
    DataApplicationMapper dataApplicationMapper;

    @Override
    public Integer createDataApplication(DataApplication dataApplication) {
        return null;
    }

    @Override
    public void updateDataApplication(DataApplication dataApplication) {

    }

    @Override
    public void deleteDataApplication(List<Integer> ids) {

    }
}
