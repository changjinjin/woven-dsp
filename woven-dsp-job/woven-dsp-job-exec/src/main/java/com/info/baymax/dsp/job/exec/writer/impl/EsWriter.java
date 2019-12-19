package com.info.baymax.dsp.job.exec.writer.impl;

import com.info.baymax.dsp.job.exec.constant.Storage;
import com.info.baymax.dsp.job.exec.writer.CommonWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: haijun
 * @Date: 2019/12/19 17:44
 */
@Slf4j
public class EsWriter implements CommonWriter {
    @Override
    public String getType() {
        return Storage.ELASTICSEARCH.getValue();
    }
}
