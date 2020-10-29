package com.info.baymax.dsp.job.exec.reader.impl;

import com.info.baymax.dsp.data.consumer.constant.TargetStorage;
import com.info.baymax.dsp.job.exec.reader.CommonReader;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: haijun
 * @Date: 2019/12/19 17:58
 */
@Slf4j
public class HBaseReader implements CommonReader {
    @Override
    public String getType() {
        return TargetStorage.HBASE.getValue();
    }
}
