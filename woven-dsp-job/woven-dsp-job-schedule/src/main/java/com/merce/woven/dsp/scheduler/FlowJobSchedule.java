package com.merce.woven.dsp.scheduler;

/**
 * @Author: haijun
 * @Date: 2019/12/12 15:22
 */
public class FlowJobSchedule implements JobSchedule {
    private String type = "flow";

    @Override
    public String getJobType(){
        return type;
    }

    @Override
    public void sendJob(String message) {

    }
}
