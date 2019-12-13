package com.info.baymax.common.mybatis.type.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import com.info.baymax.common.enums.valuable.ShortValuable;


/**
 * Short value类型枚举处理器
 * @author jingwei.yang
 * @date 2019-05-28 13:59
 * @param <E> 枚举类
 */
public class ShortValuableEnumTypeHandler<E extends Enum<E>> extends AbstractValuableEnumTypeHandler<E, Short> {

	public ShortValuableEnumTypeHandler(Class<E> type) {
		super(type);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		ShortValuable valueEnum = (ShortValuable) parameter;
		ps.setShort(i, valueEnum.getValue());
	}

	@Override
	public Short getResultValue(ResultSet rs, String columnName) throws SQLException {
		return rs.getShort(columnName);
	}

	@Override
	public Short getResultValue(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getShort(columnIndex);
	}

	@Override
	public Short getResultValue(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getShort(columnIndex);
	}

}
