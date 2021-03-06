package com.info.baymax.common.persistence.mybatis.type.enums;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 获取结果方法接口
 *
 * @param <T> 转化类型
 * @author jingwei.yang
 * @date 2019-05-28 13:58
 */
public interface ResultValuableHandler<T> {

    T getResultValue(ResultSet rs, String columnName) throws SQLException;

    T getResultValue(ResultSet rs, int columnIndex) throws SQLException;

    T getResultValue(CallableStatement cs, int columnIndex) throws SQLException;

}
