package com.info.baymax.common.cache.simple;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityCache implements Serializable {
    private static final long serialVersionUID = 3562781473518235436L;

    /**
     * 保存的数据
     */
    private Object datas;

    /**
     * 设置数据失效时间,为0表示永不失效
     */
    private long timeOut;

    /**
     * 最后刷新时间
     */
    private long lastRefeshTime;
}
