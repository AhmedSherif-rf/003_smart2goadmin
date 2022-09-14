package com.ntg.sadmin.config.tenant;

import java.beans.PropertyVetoException;
import java.util.HashMap;

import javax.sql.DataSource;

import com.ntg.sadmin.common.NTGMessageOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ntg.sadmin.DBMigration;

@Configuration
@EnableConfigurationProperties(MultitenancyConfigurationProperties.class)
public class MultitenancyConfiguration {

	@Autowired
	private MultitenancyConfigurationProperties multitenancyProperties;

	@Autowired
	private Environment environment;

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	String SchemaName;

	@Bean(name = "multitenantProvider")
	public DataSourceBasedMultiTenantConnectionProviderImpl dataSourceBasedMultiTenantConnectionProvider() {
		HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();
		multitenancyProperties.getTenants().stream().forEach(
				tc -> dataSources.put(tc.getName(), getDatasource(tc.getDriverClassName(),
						tc.getUsername(), tc.getPassword(), tc.getUrl())));

		return new DataSourceBasedMultiTenantConnectionProviderImpl(
				multitenancyProperties.getDefaultTenant().getName(), dataSources);
	}

	private DataSource getDatasource(String driverClassName, String username,
			String password, String url) {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			
			dataSource.setMinPoolSize(Integer.parseInt(
					environment.getRequiredProperty("spring.datasource.initial-size")));
			dataSource.setMaxPoolSize(Integer.parseInt(
					environment.getRequiredProperty("spring.datasource.max-active")));
			dataSource.setJdbcUrl(url);
			dataSource.setUser(username);
			dataSource.setPassword(password);
			dataSource.setDriverClass(driverClassName);
		}
		catch (PropertyVetoException e) {
			NTGMessageOperation.PrintErrorTrace(e);
		}
		return dataSource;
	}

	@Bean
	@DependsOn("multitenantProvider")
	public DataSource defaultDataSource() {
		return dataSourceBasedMultiTenantConnectionProvider().getDefaultDataSource();
	}

	@Bean
	@DependsOn("multitenantProvider")
	public DBMigration dbMigration() {
		return new DBMigration(multitenancyProperties,
				dataSourceBasedMultiTenantConnectionProvider(),SchemaName);
	}
}
