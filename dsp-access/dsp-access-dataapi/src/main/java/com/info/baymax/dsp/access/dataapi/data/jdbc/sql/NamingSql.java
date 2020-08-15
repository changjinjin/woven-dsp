package com.info.baymax.dsp.access.dataapi.data.jdbc.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分析命名SQL语句获取抽象NSQl实例；java(JDBC)提供SQL语句命名参数而是通过?标识参数位置，
 * 通过此对象可以命名参数方式使用SQL语句，命名参数以?开始后跟名称?name
 *
 * <pre>
 * SQL:
 * select * from user where name like '%zhang%' and age > 12 and gender = 'M'
 * </pre>
 */
@NoArgsConstructor
public class NamingSql {

    public NamingSql(String namingSql, Map<String, Object> paramMap) {
        this.namingSql = namingSql;
        this.paramMap = paramMap;
    }

    /**
     * 命名条件sql
     *
     * <pre>
     * 如：
     * select * from user where name like ?param0 and age > ?param1 and gender = ?param2
     * </pre>
     */
    @Getter
    protected String namingSql;

    /**
     * 参数名称与值映射
     *
     * <pre>
     * 如：
     * {
     *    "param0": "%zhang%",
     *    "param1": 12,
     *    "param2": "M"
     * }
     * </pre>
     */
    @Getter
    protected Map<String, Object> paramMap = new HashMap<String, Object>();

    /**
     * 有占位符的sql
     *
     * <pre>
     * 如：
     * select * from user where name like ? and age > ? and gender = ?
     * </pre>
     */
    @Getter
    protected String placeholderSql;

    /**
     * 参数名称列表
     *
     * <pre>
     * 如：
     *  [param0, param1, param2]
     * </pre>
     */
    @Getter
    protected String[] paramNames;

    /**
     * 参数值列表
     *
     * <pre>
     * 如：
     *  ["%zhang%", 12, "M"]
     * </pre>
     */
    @Getter
    protected Object[] paramValues;

    public void parse(String sql, Map<String, Object> params) {
        this.namingSql = trimAndOr(sql);
        this.paramMap = params;
        char c;
        List<String> names = new ArrayList<String>();
        StringBuilder sql_builder = new StringBuilder();
        StringBuilder name_builder = new StringBuilder();
        for (int index = 0; index < namingSql.length(); index++) {
            c = namingSql.charAt(index);
            sql_builder.append(c);
            if ('?' == c) {
                while (++index < namingSql.length()) {
                    c = namingSql.charAt(index);
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || (c >= '0' && c <= '9')) {
                        name_builder.append(c);
                    } else {
                        sql_builder.append(c);
                        break;
                    }
                }
                names.add(name_builder.toString());
                name_builder.setLength(0);
            }
        }
        placeholderSql = sql_builder.toString();
        if (names != null && names.size() > 0) {
            paramNames = names.toArray(paramNames = new String[names.size()]);
            int mapSize = paramMap.size();
            if (names.size() != mapSize) {
                throw new IllegalArgumentException(
                        "Wrong number of parameters: expected " + names.size() + ", was given " + mapSize);
            }
            paramValues = new Object[mapSize];
            for (int i = 0; i < paramValues.length; i++) {
                paramValues[i] = paramMap.get(names.get(i));
            }
        }
    }

    protected String trimAndOr(String sql) {
        return StringUtils.removeStartIgnoreCase(StringUtils.removeStartIgnoreCase(StringUtils.trim(sql), "AND "),
                "OR ");
    }
}
