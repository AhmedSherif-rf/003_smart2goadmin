package com.ntg.sadmin.data.service;

import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.common.dbcompatibilityhelper.SqlHelper;
import com.ntg.sadmin.data.entities.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ntg.sadmin.constants.CommonConstants;
import com.ntg.sadmin.data.repositories.ConfigurationRepository;
import com.ntg.sadmin.utils.Utils;

import java.util.HashMap;

/**
 * ConfigurationEntityService used to communicate with ConfigurationEntity
 *
 * @author mashour@ntgclarity.com
 */
@Service
public class ConfigurationEntityService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SqlHelper sqlhelper;
    /**
     * Used to query the database by key and this function is cache able
     * <p>
     *
     * @param key
     * @return Configuration record
     */
    static HashMap<String, String> cache = new HashMap<>();

    public String getByKey(String key) {
        String re = cache.get(key);
        if (re != null) {
            return re;
        }

        if (Utils.loadFromPropertyFile(CommonConstants.APPLICATION_PROPERTIES_FILE_NAME, key) != null) {
            re = Utils.loadFromPropertyFile(CommonConstants.APPLICATION_PROPERTIES_FILE_NAME, key);
        } else {
            Configuration val = null;
            if (this.sqlhelper.getConnectionType() == 1) {
                val = configurationRepository.getByKeyOracle(key, TenantContext.getCompanyName());
            } else {
                val = configurationRepository.getByKeyPostgres(key, TenantContext.getCompanyName());
            }

            re = (val == null) ? "" : val.getValue();
        }

        cache.put(key, re);
        return re;
    }

    /**
     * Used to clear the configuration cache able
     * <p>
     */
    public void evictGetByKey() {
        cache.clear();
    }
}
