package com.info.baymax.common.mybatis.type.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import com.info.baymax.common.enums.valuable.LongValuable;


/**
 *
 * Long value类型枚举处理器
 * @author jingwei.yang
 * @date 2019-05-28 13:58
 * @param <E> 枚举类
 */
public class LongValuableEnumTypeHandler<E extends Enum<E>> extends AbstractValuableEnumTypeHandler<E, Long> {

	public LongValuableEnumTypeHandler(Class<E> type) {
		super(type);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		LongValuable valueEnum = (LongValuable) parameter;
		ps.setLong(i, valueEnum.getValue());
	}

	@Override
	public Long getResultValue(ResultSet rs, String columnName) throws SQLException {
		return rs.getLong(columnName);
	}

	@Override
	public Long getResultValue(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}

	@Override
	public Long getResultValue(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getLong(columnIndex);
	}
}
