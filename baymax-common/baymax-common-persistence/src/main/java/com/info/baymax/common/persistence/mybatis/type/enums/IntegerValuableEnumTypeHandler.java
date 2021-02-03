package com.info.baymax.common.persistence.mybatis.type.enums;

import com.info.baymax.common.core.enums.valuable.IntegerValuable;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Integer value类型枚举处理器
 *
 * @param <E> 枚举类
 * @author jingwei.yang
 * @date 2019-05-28 13:58
 */
public class IntegerValuableEnumTypeHandler<E extends Enum<E>> extends AbstractValuableEnumTypeHandler<E, Integer> {

    public IntegerValuableEnumTypeHandler(Class<E> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        IntegerValuable valueEnum = (IntegerValuable) parameter;
        ps.setInt(i, valueEnum.getValue());
    }

    @Override
    public Integer getResultValue(ResultSet rs, String columnName) throws SQLException {
        return rs.getInt(columnName);
    }

    @Override
    public Integer getResultValue(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }

    @Override
    public Integer getResultValue(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getInt(columnIndex);
    }
}
