package com.info.baymax.common.mybatis.type.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

import com.info.baymax.common.enums.valuable.BooleanValuable;
/**
 * Boolean value类型枚举处理器
 * @author jingwei.yang
 * @date 2019-05-28 13:57
 * @param <E> 枚举类
 */
public class BooleanValuableEnumTypeHandler<E extends Enum<E>> extends AbstractValuableEnumTypeHandler<E, Boolean> {

	public BooleanValuableEnumTypeHandler(Class<E> type) {
		super(type);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		BooleanValuable valueEnum = (BooleanValuable) parameter;
		ps.setBoolean(i, valueEnum.getValue());
	}

	@Override
	public Boolean getResultValue(ResultSet rs, String columnName) throws SQLException {
		return rs.getBoolean(columnName);
	}

	@Override
	public Boolean getResultValue(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getBoolean(columnIndex);
	}

	@Override
	public Boolean getResultValue(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getBoolean(columnIndex);
	}

}
