package com.ntg.sadmin.config.tenant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.ntg.sadmin.common.NTGMessageOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.DefaultPropertiesPersister;

import com.ntg.sadmin.Application;

@ConfigurationProperties(prefix = "multitenancy")
public class MultitenancyConfigurationProperties {


    private Tenant defaultTenant;

    private List<Tenant> tenants = new ArrayList<Tenant>();

    private static Environment environment;

    public static Environment getEnvironment() {
        return environment;
    }

    @Autowired
    public static void setEnvironment(Environment environment) {
        MultitenancyConfigurationProperties.environment = environment;
    }

    public static HashMap<String, Tenant> TeentList = new HashMap<String, Tenant>();

    @PostConstruct
    public void init() {
        List<Tenant> tcs = tenants.stream().filter(tc -> tc.isDefault()).collect(Collectors.toCollection(ArrayList::new));
        if (tcs.size() > 1) {
            throw new IllegalStateException("Only can be configured as default one data source. Review your configuration");
        }
        this.defaultTenant = tcs.get(0);
        for (int tenantIndex = 0; tenantIndex < tenants.size(); tenantIndex++) {
            tenants = intializeSettings(tenants, tenantIndex);
        }
        // fill tenet map for fture use
        for (Tenant t : tenants) {
            TeentList.put(t.getName(), t);
        }
    }

    public static List<Tenant> intializeSettings(List<Tenant> tenants, int index) {
        try {
            Properties propsToWrite = Application.intializeSettings("application_sadmin.properties", true, false);
            if (propsToWrite != null) {

                tenants.get(index).setDefault(Boolean.parseBoolean(propsToWrite.getProperty("multitenancy.tenants[" + index + "].default")));

                tenants.get(index).setDriverClassName(propsToWrite.getProperty("multitenancy.tenants[" + index + "].driver-class-name"));

                tenants.get(index).setName(propsToWrite.getProperty("multitenancy.tenants[" + index + "].name"));

                tenants.get(index).setPassword(propsToWrite.getProperty("multitenancy.tenants[" + index + "].password"));

                tenants.get(index).setUrl(propsToWrite.getProperty("multitenancy.tenants[" + index + "].url"));

                tenants.get(index).setUsername(propsToWrite.getProperty("multitenancy.tenants[" + index + "].username"));


            }
        } catch (Exception e) {
            NTGMessageOperation.PrintErrorTrace(e);
        }
        return tenants;
    }

    public static HashMap<String, Tenant> getTeentList() {
        return TeentList;
    }

    public static void setTeentList(HashMap<String, Tenant> teentList) {
        TeentList = teentList;
    }

    public void setDefaultTenant(Tenant defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    public Tenant getDefaultTenant() {
        return defaultTenant;
    }

    public static class Tenant {


        private String name;

        private boolean isDefault;

        private String driverClassName;

        private String url;

        private String username;

        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }
    }
}
