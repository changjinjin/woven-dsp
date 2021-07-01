package com.info.baymax.common.persistence.mybatis.type;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

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

    /**
     * 将blob字段转化为实体对象
     *
     * @param clob clob字段值
     * @return 结果实体对象
     * @throws SQLException
     * @throws IOException
     */
    public T getNullableResult(Clob clob) throws SQLException {
        if (null == clob) {
            return null;
        }

        try {
            // 把byte转化成string，并将字符串反序列化成需要的java bean对象
            return translate2Bean(clob2Str(clob));
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    /**
     * clob 转成String
     *
     * @param clob clob数据
     * @return clob转换成的字符串
     * @throws SQLException
     * @throws IOException
     */
    public String clob2Str(Clob clob) throws SQLException, IOException {
        return clob == null ? null : clob.getSubString(1, (int) clob.length());
    }

    /**
     * 将blob字段转化为实体对象
     *
     * @param blob blob字段值
     * @return 结果实体对象
     * @throws SQLException
     */
    public T getNullableResult(Blob blob) throws SQLException {
        if (null == blob) {
            return null;
        }

        try {
            // 把byte转化成string，并将字符串反序列化成需要的java bean对象
            return translate2Bean(new String(blob.getBytes(1, (int) blob.length()), DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    /**
     * 将Varchar数据转化为实体对象. <br>
     *
     * @param result 字符字段值
     * @return 转化后实体对象
     * @throws SQLException
     */
    public T getNullableResult(String result) throws SQLException {
        try {
            if (StringUtils.isNotEmpty(result)) {
                return translate2Bean(result);
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return null;
    }
}
