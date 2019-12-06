package com.merce.woven.common.mybatis.type.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import com.merce.woven.common.enums.valuable.ByteValuable;


/**
 * Byte value类型枚举处理器
 * @author jingwei.yang
 * @date 2019-05-28 13:57
 * @param <E> 枚举类
 */
public class ByteValuableEnumTypeHandler<E extends Enum<E>> extends AbstractValuableEnumTypeHandler<E, Byte> {
	public ByteValuableEnumTypeHandler(Class<E> type) {
		super(type);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		ByteValuable valueEnum = (ByteValuable) parameter;
		ps.setByte(i, valueEnum.getValue());
	}

	@Override
	public Byte getResultValue(ResultSet rs, String columnName) throws SQLException {
		return rs.getByte(columnName);
	}

	@Override
	public Byte getResultValue(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getByte(columnIndex);
	}

	@Override
	public Byte getResultValue(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getByte(columnIndex);
	}

}
