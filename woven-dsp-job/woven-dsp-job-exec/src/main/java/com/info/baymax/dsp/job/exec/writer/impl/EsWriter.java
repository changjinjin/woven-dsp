package com.info.baymax.dsp.job.exec.writer.impl;

import com.info.baymax.dsp.data.consumer.constant.TargetStorage;
import com.info.baymax.dsp.job.exec.writer.CommonWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: haijun
 * @Date: 2019/12/19 17:44
 */
@Slf4j
public class EsWriter implements CommonWriter {
    @Override
    public String getType() {
        return TargetStorage.ELASTICSEARCH.getValue();
    }
}
