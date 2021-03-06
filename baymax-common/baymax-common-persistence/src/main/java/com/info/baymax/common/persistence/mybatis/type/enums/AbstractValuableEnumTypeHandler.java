package com.info.baymax.common.persistence.mybatis.type.enums;

import com.info.baymax.common.core.enums.valuable.Valuable;
import org.apache.ibatis.type.BaseTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Value类型枚举处理器
 *
 * @param <E> 枚举类
 * @param <V> 目标类
 * @author jingwei.yang
 * @date 2019-05-28 13:56
 */
public abstract class AbstractValuableEnumTypeHandler<E extends Enum<E>, V> extends BaseTypeHandler<E>
    implements ResultValuableHandler<V> {

    private Class<E> type;
    private Map<V, E> map = new HashMap<V, E>();

    @SuppressWarnings("unchecked")
    public AbstractValuableEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        E[] enums = type.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
        Valuable<V> valueEnum = null;
        for (E e : enums) {
            valueEnum = (Valuable<V>) e;
            map.put(valueEnum.getValue(), e);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (!rs.wasNull()) {
            return getNullableResult(getResultValue(rs, columnName));
        }
        return null;
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (!rs.wasNull()) {
            return getNullableResult(getResultValue(rs, columnIndex));
        }
        return null;
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (!cs.wasNull()) {
            return getNullableResult(getResultValue(cs, columnIndex));
        }
        return null;
    }

    public E getNullableResult(V value) {
        try {
            return map.get(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName() + " by value.",
                e);
        }
    }
}
