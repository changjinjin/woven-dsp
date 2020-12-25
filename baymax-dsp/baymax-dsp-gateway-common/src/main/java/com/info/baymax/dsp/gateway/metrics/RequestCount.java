package com.info.baymax.dsp.gateway.metrics;

import lombok.Data;

@Data
public class RequestCount {
    String path;
    int count = 0;
    double avgtimetaken = 0.0;
    long maxtimetaken = 0L;
    long mintimetaken = 0L;
    long alltimetaken = 0L;

    public void setCount(int count) {
        this.count += count;
    }

    public void setTimeTaken(long timeTaken) {
        this.alltimetaken += timeTaken;
        if (this.maxtimetaken < timeTaken) {
            this.maxtimetaken = timeTaken;
        }
        if (this.mintimetaken > timeTaken || this.mintimetaken == 0L) {
            this.mintimetaken = timeTaken;
        }
        this.avgtimetaken = (alltimetaken * 1.0) / count;
    }
}
