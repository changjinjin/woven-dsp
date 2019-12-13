package com.merce.woven.dsp.api;

/**
 * @Author: haijun
 * @Date: 2019/12/12 15:10
 */
public interface PushInterface {
    String getEngine();
    void pushMessage(String message);
}
