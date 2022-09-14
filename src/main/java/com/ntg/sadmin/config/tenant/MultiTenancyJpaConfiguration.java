package com.ntg.sadmin.config.tenant;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableConfigurationProperties(JpaProperties.class)
public class MultiTenancyJpaConfiguration {

	@Value("${c3p.IDLE_TEST_PERIOD}")
	private int idleTestPeriod;

	@Value("${c3p.MIN_POOL_SIZE}")
	private int minPoolSize;

	@Value("${c3p.MAX_POOL_SIZE}")
	private int maxPoolSize;

	@Value("${c3p.ACQUIRE_INCREMENT}")
	private int poolIncrementSize;

	@Value("${c3p.MAX_STATEMENTS}")
	private int maxCachedStatements;

	@Value("${c3p.TIMEOUT}")
	private int connectionTimeoutCheckPeriod;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private MultiTenantConnectionProvider multiTenantConnectionProvider;

	@Autowired
	private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> hibernateProps = new LinkedHashMap<String, Object>();
		hibernateProps.putAll(jpaProperties.getProperties());

		hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
		hibernateProps.put("hibernate.id.new_generator_mappings", Boolean.toString(true));

		// c3p0 configuration
		hibernateProps.put(Environment.C3P0_IDLE_TEST_PERIOD,  idleTestPeriod); //time to check connection validity
		hibernateProps.put(Environment.C3P0_MIN_SIZE, minPoolSize); // Minimum size of pool
		hibernateProps.put(Environment.C3P0_MAX_SIZE, maxPoolSize); // Maximum size of pool
		hibernateProps.put(Environment.C3P0_ACQUIRE_INCREMENT, poolIncrementSize);// Number of connections acquired at a time when pool is exhausted
		hibernateProps.put(Environment.C3P0_TIMEOUT, connectionTimeoutCheckPeriod); // Connection idle time
		hibernateProps.put(Environment.C3P0_MAX_STATEMENTS, maxCachedStatements); // PreparedStatement cache size
		return builder.dataSource(dataSource).packages("com.ntg").properties(hibernateProps).jta(false).build();
	}
}
