package com.info.baymax.dsp.data.consumer.service;

import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;

public interface DataCustAppService extends BaseEntityService<DataCustApp> {

    /**
     * 根据accessKey查询记录
     *
     * @param accessKey accessKey
     * @return 匹配accessKey的唯一记录
     */
    DataCustApp selectByAccessKeyNotNull(String accessKey);

}
