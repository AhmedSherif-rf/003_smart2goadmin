package com.ntg.sadmin.common.dbcompatibilityhelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.config.tenant.MultitenancyConfigurationProperties;
import com.ntg.sadmin.config.tenant.MultitenancyConfigurationProperties.Tenant;

/**
 * SqlHelper we'll be using to utilize any function related to database
 * 
 * 
 * @author yghandor@ntgclarity.com
 * 
 */
@Service
public class SqlHelper {

	private static final Logger logger = LoggerFactory.getLogger(SqlHelper.class);

	/**
	 * <p>
	 * Get Connection Type of database
	 * 
	 * @return 1 for ORACLE and 2 for POSTGRESQL
	 * @since 1.0
	 */
	public int getConnectionType() {
		logger.info("start getConnectionType function");
		String TenetName = (String) TenantContext.getCurrentTenant();
		Tenant tenetInfo = MultitenancyConfigurationProperties.TeentList.get(TenetName);

		if (tenetInfo == null) {
			TenetName = (String) MultitenancyConfigurationProperties.TeentList.keySet().toArray()[0];
			tenetInfo = MultitenancyConfigurationProperties.TeentList.get(TenetName);
		}
		int connectionType = (tenetInfo.getUrl().toLowerCase().indexOf("oracle") > -1) ? 1 : 2;
		logger.info("end getConnectionType function");
		return connectionType;
	}

	public String SequenceFetch(String SequenceName) {

		String Sql = "";
		if (this.getConnectionType() == 1) {// oracle
			Sql = SequenceName + ".nextVal ";
		} else { // postgres syntax
			Sql = "nextval('" + SequenceName + "')";
		}
		return Sql;
	}

}
