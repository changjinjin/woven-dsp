package com.info.baymax.common.persistence.mybatis.type.blob;

import com.info.baymax.common.persistence.mybatis.type.AbstractComplexTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;

/**
 * Blob数据类型处理器抽象
 *
 * @author jingwei.yang
 * @date 2019-05-28 14:00
 */
public abstract class AbstractBlobTypeHandler<T> extends AbstractComplexTypeHandler<T> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T t, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis;
        String str = null;
        try {
            // 将java bean对象序列化成字符串对象
            str = translate2Str(t);
            // 把String转化成byte流
            bis = new ByteArrayInputStream(str.getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
        ps.setBinaryStream(i, bis, str.length());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResult(rs.getBlob(columnName));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getNullableResult(cs.getBlob(columnIndex));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnName) throws SQLException {
        return getNullableResult(rs.getBlob(columnName));
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

}
