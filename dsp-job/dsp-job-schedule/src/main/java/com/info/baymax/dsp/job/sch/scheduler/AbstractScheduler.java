package com.info.baymax.dsp.job.sch.scheduler;

import org.quartz.Job;

/**
 * Schedule Flow with Quartz
 */
public interface AbstractScheduler<T> {

    void schedule(T fs, Class<? extends Job> jobClass);

    void cancel(T fs);
}
