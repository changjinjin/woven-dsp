package com.info.baymax.dsp.data.consumer.service;

import com.info.baymax.dsp.data.consumer.entity.DataApplication;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataApplicationService {
    Integer createDataApplication(DataApplication dataApplication);
    void updateDataApplication(DataApplication dataApplication);
    void deleteDataApplication(List<Integer> ids);
}
