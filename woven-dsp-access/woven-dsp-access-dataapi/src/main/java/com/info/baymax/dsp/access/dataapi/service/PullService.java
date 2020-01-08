package com.info.baymax.dsp.access.dataapi.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
public interface PullService {

    List<Map<String, Object>> query(String storage, Map<String, String> fieldMap, Map<String, String> conf, int offset,
                                    int size, String[] includes);

}
