package com.info.baymax.common.mybatis.type.clob;

import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

import com.info.baymax.common.mybatis.type.AbstractComplexTypeHandler;

/**
 * Clob数据类型处理器抽象
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 14:00
 */
public abstract class AbstractClobTypeHandler<T> extends AbstractComplexTypeHandler<T> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T t, JdbcType jdbcType) throws SQLException {
        String str = translate2Str(t);
        if (str != null) {
            final StringReader reader = new StringReader(str);
            ps.setCharacterStream(i, reader, str.getBytes().length);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResult(rs.getClob(columnName));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getNullableResult(cs.getClob(columnIndex));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnName) throws SQLException {
        return getNullableResult(rs.getClob(columnName));
    }

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

}
