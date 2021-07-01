package com.info.baymax.common.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tk.mybatis.mapper.entity.Config;

@Getter
@Setter
@ConfigurationProperties(prefix = "mapper")
public class MapperProperties extends Config {

	/**
	 * 对应数据源mapper包路径
	 */
	private String[] basePackages;

}
