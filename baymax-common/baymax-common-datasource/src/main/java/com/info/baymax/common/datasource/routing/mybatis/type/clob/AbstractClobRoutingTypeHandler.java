package com.info.baymax.common.datasource.routing.mybatis.type.clob;

import com.info.baymax.common.datasource.routing.lookup.Dialect;
import com.info.baymax.common.datasource.routing.lookup.DialectContext;
import com.info.baymax.common.persistence.mybatis.type.AbstractComplexTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clob和Varchar类型模糊适配的数据类型处理器抽象类
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 14:00
 */
public abstract class AbstractClobRoutingTypeHandler<T> extends AbstractComplexTypeHandler<T> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T t, JdbcType jdbcType) throws SQLException {
		String str = translate2Str(t);
		if (str != null) {
			switch (JdbcType.forCode(jdbcType.TYPE_CODE)) {
				case CLOB:
					final StringReader reader = new StringReader(str);
					ps.setCharacterStream(i, reader, str.getBytes().length);
					break;
				default:
					ps.setString(i, str);
					break;
			}
		}
	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Dialect dialect = DialectContext.get();
		switch (dialect) {
			case elasticsearch:
				return getNullableResult(rs.getString(columnName));
			default:
				return getNullableResult(rs.getClob(columnName));
		}
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Dialect dialect = DialectContext.get();
		switch (dialect) {
			case elasticsearch:
				return getNullableResult(cs.getString(columnIndex));
			default:
				return getNullableResult(cs.getClob(columnIndex));
		}
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnName) throws SQLException {
		Dialect dialect = DialectContext.get();
		switch (dialect) {
			case elasticsearch:
				return getNullableResult(rs.getString(columnName));
			default:
				return getNullableResult(rs.getClob(columnName));
		}
	}
}
