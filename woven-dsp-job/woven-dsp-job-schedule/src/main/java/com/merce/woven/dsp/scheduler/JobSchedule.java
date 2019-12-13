package com.merce.woven.dsp.scheduler;

/**
 * @Author: haijun
 * @Date: 2019/12/12 15:10
 */
public interface JobSchedule {
    String getJobType();
    void sendJob(String message);
}
