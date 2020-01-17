package com.info.baymax.dsp.data.consumer.constant;

/**
 * @Author: haijun
 * @Date: 2019/12/19 11:48
 */
public class ScheduleJobStatus {
    //0 就绪,1 运行中 2 失败 3成功 4待停止 5已停止
    public static final Integer JOB_STATUS_READY = 0;
    public static final Integer JOB_STATUS_RUNNING = 1;
    public static final Integer JOB_STATUS_FAILED = 2;
    public static final Integer JOB_STATUS_SUCCEED = 3;
    public static final Integer JOB_STATUS_TO_STOP = 4;
    public static final Integer JOB_STATUS_STOPPED = 5;
}
