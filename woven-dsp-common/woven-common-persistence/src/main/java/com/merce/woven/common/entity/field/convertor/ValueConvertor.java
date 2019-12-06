package com.merce.woven.common.entity.field.convertor;

public interface ValueConvertor<T> {

    /**
     * 获取真实类型的默认值
     *
     * @param defaultValue 字符类型的默认值
     * @return 真实类型的默认值
     */
    T convert(String defaultValue);
}
