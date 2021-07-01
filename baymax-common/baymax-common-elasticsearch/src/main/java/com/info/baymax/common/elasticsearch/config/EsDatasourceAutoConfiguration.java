package com.info.baymax.common.elasticsearch.config;

import com.info.baymax.common.core.annotation.condition.ConditionalOnPropertyNotEmpty;
import com.info.baymax.common.datasource.AbstractDataSourceConfiguration;
import com.info.baymax.common.datasource.MapperProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@ConditionalOnPropertyNotEmpty("spring.es-datasource.url")
@MapperScan(basePackages = "${spring.es-mybatis.mapper.base-packages}", sqlSessionFactoryRef = "esSqlSessionFactory", sqlSessionTemplateRef = "esSqlSessionTemplate", mapperHelperRef = "esMapperHelper", properties = {
		"identity=elasticsearch", "style=camelhumpAndLowercase" })
public class EsDatasourceAutoConfiguration extends AbstractDataSourceConfiguration {

	@Bean(name = "esDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.es-datasource")
	public DataSourceProperties esDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "esDataSource")
	@ConfigurationProperties(prefix = "spring.es-datasource.hikari")
	public HikariDataSource dataSource(@Qualifier("esDataSourceProperties") DataSourceProperties properties) {
		HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
		if (StringUtils.hasText(properties.getName())) {
			dataSource.setPoolName(properties.getName());
		}
		return dataSource;
	}

	@Bean(name = "esTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("esDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "esSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("esDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}

	@Bean(name = "esSqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("esSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Configuration
	public static class EsMybatisMapperConfiguration {

		@Bean(name = "esMybatisMapperProperties")
		@ConfigurationProperties(prefix = "spring.es-mybatis.mapper")
		public MapperProperties esMybatisMapperProperties() {
			return new MapperProperties();
		}

		@Bean(name = "esMapperHelper")
		public MapperHelper esMapperHelper(@Qualifier("mapperProperties") @Nullable MapperProperties mapperProperties,
				@Qualifier("esMybatisMapperProperties") MapperProperties esMybatisMapperProperties) {
			MapperHelper mapperHelper = new MapperHelper();
			if (esMybatisMapperProperties != null) {
				mapperHelper.setConfig(esMybatisMapperProperties);
			} else {
				mapperHelper.setConfig(mapperProperties);
			}
			return mapperHelper;
		}
	}
}
