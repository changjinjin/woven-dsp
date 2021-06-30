package com.info.baymax.common.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

@Getter
@Setter
public class DataSourceMapperProperties extends DataSourceProperties {

	/**
	 * 数据源对应的mapper包路径数组
	 */
	private String[] mapperBasePackages;

}
