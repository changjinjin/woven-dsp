package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.dsp.data.platform.entity.DataResource;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataResourceService {
    Integer createDataResource(DataResource dataResource);
    void updateDataResource(DataResource dataResource);
    void deleteDataResource(List<Integer> ids);
}
