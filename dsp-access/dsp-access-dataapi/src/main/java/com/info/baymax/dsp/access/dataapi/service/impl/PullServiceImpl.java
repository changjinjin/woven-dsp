package com.info.baymax.dsp.access.dataapi.service.impl;

import com.info.baymax.common.page.IPage;
import com.info.baymax.dsp.access.dataapi.data.DataReader;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.Query;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
@Service
public class PullServiceImpl implements PullService {

    @Autowired
    private DataReader<MapEntity> dataReader;

    @Override
    public IPage<MapEntity> query(String storage, Map<String, String> fieldMap, Map<String, String> conf, Query query) {
        conf.put("storage", storage);
        return dataReader.read(StorageConf.from(conf), query);
    }
}
