package com.info.baymax.dsp.job.sch.scheduler;

import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.springframework.stereotype.Component;

/**
 * @Author: haijun
 * @Date: 2019/12/12 15:22
 */
@Component
@Slf4j
public class FlowScheduler implements AbstractScheduler<Dataset>  {
    private String type = "flow";

    @Override
    public void schedule(Dataset fs, Class<? extends Job> jobClass) {

    }

    @Override
    public void cancel(Dataset fs) {

    }
}
