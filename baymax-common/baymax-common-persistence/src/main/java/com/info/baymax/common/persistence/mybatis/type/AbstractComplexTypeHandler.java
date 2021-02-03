package com.info.baymax.common.persistence.mybatis.type;

import org.apache.ibatis.type.BaseTypeHandler;

public abstract class AbstractComplexTypeHandler<T> extends BaseTypeHandler<T> implements JsonFormatTypeHandler {

    /**
     * 指定默认字符集
     */
    protected static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 默认的字符件的分隔符
     */
    protected static final String DEFAULT_REGEX = ",";

    /**
     * 说明： 将实体序列化为字符串. <br>
     *
     * @param t 实体对象
     * @return 转化后的字符串
     */
    public abstract String translate2Str(T t);

    /**
     * 说明： 将序列化的数据转成的字符串转化成java对象. <br>
     *
     * @param result 结果字符串
     * @return java对象
     */
    public abstract T translate2Bean(String result);
}
