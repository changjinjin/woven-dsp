package com.info.baymax.common.persistence.mybatis.type.fuzzy;

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
public abstract class AbstractVarcharClobFuzzyTypeHandler<T> extends AbstractComplexTypeHandler<T> {

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
		try {
			return getNullableResult(rs.getString(columnName));
		} catch (Exception e) {
			return getNullableResult(rs.getClob(columnName));
		}
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		try {
			return getNullableResult(cs.getString(columnIndex));
		} catch (Exception e) {
			return getNullableResult(cs.getClob(columnIndex));
		}
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnName) throws SQLException {
		try {
			return getNullableResult(rs.getString(columnName));
		} catch (Exception e) {
			return getNullableResult(rs.getClob(columnName));
		}
	}

}
