package com.info.baymax.dsp.gateway.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class RequestCountTraceRepository implements HttpTraceRepository {
    private int capacity = 1000;
    private boolean reverse = true;
    private final List<HttpTrace> traces = new LinkedList<HttpTrace>();

    @Value("${management.endpoint.httptrace.summarytime:5}")
    private int summarytime;

    public RequestCountTraceRepository() {
    }

    public void setReverse(boolean reverse) {
        synchronized (this.traces) {
            this.reverse = reverse;
        }
    }

    public void setCapacity(int capacity) {
        synchronized (this.traces) {
            this.capacity = capacity;
        }
    }

    @Override
    public List<HttpTrace> findAll() {
        synchronized (this.traces) {
            return Collections.unmodifiableList(new ArrayList<HttpTrace>(this.traces));
        }
    }

    @Override
    public void add(HttpTrace trace) {
        synchronized (this.traces) {
            String path = trace.getRequest().getUri().getPath();
            if (path != null && !(path.lastIndexOf(".js") >= 0 || path.lastIndexOf(".html") >= 0
                || path.lastIndexOf(".png") >= 0 || path.lastIndexOf(".css") >= 0 || path.lastIndexOf(".gif") >= 0
                || path.lastIndexOf(".jpg") >= 0 || path.lastIndexOf(".css") >= 0
                || path.lastIndexOf("/bower_components/") >= 0 || path.lastIndexOf("/actuator/") >= 0)) {

                while (this.traces.size() >= this.capacity) {
                    this.traces.remove(this.reverse ? this.capacity - 1 : 0);
                }
                this.removeOldHttpTrace();

                if (this.reverse) {
                    this.traces.add(0, trace);
                    log.trace("Request:" + trace.getRequest().getUri() + ", Response Status:"
                        + trace.getResponse().getStatus() + ", TimeTaken:" + trace.getTimeTaken());
                } else {
                    this.traces.add(trace);
                }
            }
        }
    }

    private void removeOldHttpTrace() {
        long nowTime = new Date().getTime();
        for (int i = 0; i < this.traces.size(); i++) {
            HttpTrace tr = this.traces.get(i);
            long requestTime = Date.from(tr.getTimestamp()).getTime();
            if (nowTime - requestTime > summarytime * 60 * 1000) {
                traces.remove(i);
            }
        }
    }
}
