package com.info.baymax.common.persistence.mybatis.type.varchar;

import com.info.baymax.common.persistence.mybatis.type.AbstractComplexTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Varchar字段处理器
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:53
 */
public abstract class AbstractVarcharTypeHandler<T> extends AbstractComplexTypeHandler<T> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T t, JdbcType jdbcType) throws SQLException {
        ps.setString(i, translate2Str(t));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResult(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getNullableResult(cs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnName) throws SQLException {
        return getNullableResult(rs.getString(columnName));
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
