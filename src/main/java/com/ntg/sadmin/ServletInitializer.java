package com.ntg.sadmin;

import com.ntg.common.STAGESystemOut;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.ntg.sadmin.config.tenant.CurrentTenantIdentifierResolverImpl;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		STAGESystemOut.OverrideSystemOutput();
		Application.ApplicaitonIsIntializing = 1;
		TenantContext.setCurrentTenantByCompanyName(CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID, "main",
				"Application.class");


		SpringApplicationBuilder re = application.sources(Application.class);
		Application.NTGInit();
		return re;
	}

}
