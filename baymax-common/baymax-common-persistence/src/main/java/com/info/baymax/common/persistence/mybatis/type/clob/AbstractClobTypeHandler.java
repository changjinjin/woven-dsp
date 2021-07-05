package com.info.baymax.common.persistence.mybatis.type.clob;

import com.info.baymax.common.persistence.mybatis.type.AbstractComplexTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
