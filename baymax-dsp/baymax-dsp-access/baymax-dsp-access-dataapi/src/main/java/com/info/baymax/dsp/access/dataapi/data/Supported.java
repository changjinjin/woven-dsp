package com.info.baymax.dsp.access.dataapi.data;

public interface Supported<T> {

    /**
     * 是否支持
     *
     * @param t 适配对象
     * @return 是否支持
     */
    boolean supports(T t);

}
