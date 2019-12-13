package com.merce.woven.dsp.service;

import com.merce.woven.dsp.scheduler.JobSchedule;

/**
 * @Author: haijun
 * @Date: 2019/12/12 15:22
 */
public class JdbcPush implements JobSchedule {
    private String engine = "jdbc";

    @Override
    public String getEngine(){
        return engine;
    }

    @Override
    public void pushMessage(String message) {

    }
}
