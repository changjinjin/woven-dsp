package com.info.baymax.common.datasource;

import com.info.baymax.common.core.annotation.condition.ConditionalOnPropertyNotEmpty;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * 多数据源的时候需要指定主数据源，这里定义默认的数据源并指定为主数据源
 *
 * @author jingwei.yang
 * @date 2021年6月25日 下午3:28:22
 */
@Primary
@Configuration
@ConditionalOnPropertyNotEmpty("spring.datasource.url")
@MapperScan(basePackages = "${spring.datasource.mapper-base-packages}", sqlSessionFactoryRef = "sqlSessionFactory", sqlSessionTemplateRef = "sqlSessionTemplate", properties = {
	"style=camelhumpAndLowercase", "enableMethodAnnotation=true", "notEmpty=false"})
public class PrimaryDataSourceConfiguration extends AbstractDataSourceConfiguration {

	@Primary
	@Bean(name = "dataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSourceMapperProperties dataSourceProperties() {
		return new DataSourceMapperProperties();
	}

	@Primary
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariDataSource dataSource(@Qualifier("dataSourceProperties") DataSourceProperties properties) {
		HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
		if (StringUtils.hasText(properties.getName())) {
			dataSource.setPoolName(properties.getName());
		}
		return dataSource;
	}

	@Primary
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Primary
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}

	@Primary
	@Bean(name = "sqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
		throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
