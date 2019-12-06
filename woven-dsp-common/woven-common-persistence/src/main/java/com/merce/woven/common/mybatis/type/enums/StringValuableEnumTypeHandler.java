package com.merce.woven.common.mybatis.type.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import com.merce.woven.common.enums.valuable.StringValuable;

/**
 * String value类型枚举处理器
 *
 * @author jingwei.yang
 * @date 2019-05-28 13:59
 * @param <E>
 *            枚举类
 */
public class StringValuableEnumTypeHandler<E extends Enum<E>> extends AbstractValuableEnumTypeHandler<E, String> {

	public StringValuableEnumTypeHandler(Class<E> type) {
		super(type);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		StringValuable valueEnum = (StringValuable) parameter;
		ps.setString(i, valueEnum.getValue());
	}

	@Override
	public String getResultValue(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}

	@Override
	public String getResultValue(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	@Override
	public String getResultValue(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getString(columnIndex);
	}

}
