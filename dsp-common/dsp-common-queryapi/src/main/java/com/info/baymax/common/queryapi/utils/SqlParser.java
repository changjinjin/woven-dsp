package com.info.baymax.common.queryapi.utils;

import java.util.List;

public class SqlParser {
	// 匹配参数: #{username} -> username
	private static final String PARAMETER_REG = "(?<=(?<!\\\\)\\#\\{)(.*?)(?=(?<!\\\\)\\})";

	public static String trim(String sql) {
		return sql.replaceAll("\r", " ").replaceAll("\n", " ");
	}

	public static List<String> findParameters(String sql) {
		return RegExp.find(PARAMETER_REG, sql);
	}

	public static String replaceParameters(String sql, String prefix) {
		List<String> parameters = findParameters(sql);
		if (parameters != null && !parameters.isEmpty()) {
			for (String parameter : parameters) {
				sql = sql.replace("#{" + parameter + "}", prefix + parameter);
			}
		}
		return sql;
	}

	public static void main(String[] args) {
		System.out.println(findParameters("a${a}a"));
		System.out.println(findParameters("a\\${a}a"));
		System.out.println(findParameters("a${a\\}a"));
		System.out.println(findParameters("a${a\\}a}a"));
		System.out.println(findParameters("a${a}a${"));
		System.out.println(findParameters("a${ab}a${a}"));
		System.out.println(findParameters("select * from user where uaername = #{username} and name like '%#{name}%'"));
		System.out.println(
				replaceParameters("select * from user where uaername = #{username} and name like '%#{name}%'", "?"));
	}
}
