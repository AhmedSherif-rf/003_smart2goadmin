package com.ntg.sadmin.config.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import com.ntg.sadmin.TenantContext;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    
	public static final String DEFAULT_TENANT_ID = "Ign";
	
     @Override
     public String resolveCurrentTenantIdentifier() {
    	 Object tenet = TenantContext.getCurrentTenant();
    	 if(tenet !=null && !("").equals((String) tenet)){

        	 return (String) tenet;
    	 }
    	 return DEFAULT_TENANT_ID;
     }
    
     @Override
     public boolean validateExistingCurrentSessions() {
         return true;
     }
}
