package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.dsp.data.platform.entity.DataResource;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataResourceService {
    String createDataResource(DataResource dataResource);
    void updateDataResource(DataResource dataResource);
    void deleteDataResource(String[] ids);
}
