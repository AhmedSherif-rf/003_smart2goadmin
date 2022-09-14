package com.ntg.sadmin.config.tenant;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final long serialVersionUID = 1L;

    private final String defaultTenant;

    private final Map<String, DataSource> map;

    public DataSourceBasedMultiTenantConnectionProviderImpl(String defaultTenant, Map<String, DataSource> map) {
        super();
        this.defaultTenant = defaultTenant;
        this.map = map;
    }

    @Override
    public DataSource selectAnyDataSource() {
     return map.get(defaultTenant);
    }

    @Override
    public DataSource selectDataSource(String tenantIdentifier) {
    	if(!"Ign".equals(tenantIdentifier)){
    		return map.get(tenantIdentifier);
    	}else {
    		return map.get(defaultTenant);
    	}
    }

    public DataSource getDefaultDataSource() {
        return map.get(defaultTenant);
    }

    
}
