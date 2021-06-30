package com.info.baymax.common.elasticsearch.config;

import com.info.baymax.common.core.annotation.condition.ConditionalOnPropertyNotEmpty;
import com.info.baymax.common.datasource.AbstractDataSourceConfiguration;
import com.info.baymax.common.datasource.DataSourceMapperProperties;
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
import org.springframework.util.StringUtils;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@ConditionalOnPropertyNotEmpty("spring.es-datasource.url")
@MapperScan(basePackages = "${spring.es-datasource.mapper-base-packages}", sqlSessionFactoryRef = "esSqlSessionFactory", sqlSessionTemplateRef = "esSqlSessionTemplate", properties = {
	"identity=elasticsearch", "style=camelhumpAndLowercase", "enableMethodAnnotation=true", "notEmpty=false"})
public class EsDatasourceAutoConfiguration extends AbstractDataSourceConfiguration {

	@Bean(name = "esDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.es-datasource")
	public DataSourceMapperProperties esDataSourceProperties() {
		return new DataSourceMapperProperties();
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
}
