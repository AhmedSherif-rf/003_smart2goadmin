package com.ntg.sadmin;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import com.ntg.sadmin.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ntg.sadmin.common.NTGEHCacher;
import com.ntg.sadmin.common.NTGObjectStreamReader;
import com.ntg.sadmin.config.tenant.CurrentTenantIdentifierResolverImpl;
import com.ntg.sadmin.web.dto.CRMSessionInfo;

public class TenantContext {
	
	public static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
	
	// to change to hold CRMSessionInfo in-replace of String or object
	private static final ThreadLocal<CRMSessionInfo> currentTenant = new ThreadLocal<CRMSessionInfo>();
	// keep a life for 10M
	private static final NTGEHCacher<String, CRMSessionInfo> UserSessionTokenCacher = new NTGEHCacher<String, CRMSessionInfo>(
			600000);
	// to set tenant company in it
	private static final HashMap<String,String> companyMap = new HashMap<String,String>();

	public static void setCurrentTenantByCompanyName(String companyName, String MethodName, String url) {
		CRMSessionInfo info = new CRMSessionInfo();
		info.companyName = companyName;
		currentTenant.set(info);

	}

	public static void setCurrentTenant(Object UserSessionTokenData, String MethodName, String url) {
		String UserSessionToken = (String) UserSessionTokenData;
		try {
			CRMSessionInfo info = UserSessionTokenCacher.get(UserSessionToken);
			if (info == null) {
				info = CovertUserSessionToken(UserSessionToken);
				ValidateTheSession(info);
				UserSessionTokenCacher.put(UserSessionToken, info);
				// just debug
				// System.out.println(UserSessionToken);
				// System.out.println("-->"+info.companyName +
				// info.loginUserName + info.sessionID);

			}
			// else{ no need to do anything here
			// // just to set the tenant value no need to do validation for
			// session info
			// //to be done
			// // System.out.println("found old data-->"+info.companyName +
			// info.loginUserName + info.sessionID);
			// }

			currentTenant.set(info);
		} catch (Exception ex) {
			logger.error("Erro Parsing UserSessionToken '" + UserSessionToken + "' From The Method '" + MethodName
					+ "@" + url + "':" + ex.getMessage());
			currentTenant.set(null);
			// may need to throw exception to avoid calling but after do the
			// weight list and restructure the classes
//			try {
//				throw new NTGException("0099", "Error Parsing Session Token :" + UserSessionToken);
//			} catch (NTGException e) {
				// TODO Auto-generated catch block
//				NTGMessageOperation.PrintErrorTrace(e);
//			}

		}
		// will be removed at the end

	}

	private static void ValidateTheSession(CRMSessionInfo info) throws Exception {
		// TODO Auto-generated method stub
		// phase two to be done to secure web service from calling without
		// authentication
	}

	private static CRMSessionInfo CovertUserSessionToken(String userSessionToken)
			throws ClassNotFoundException, IOException {
//		String[] list = userSessionToken.split("_");
//		int n = list.length;
//		byte[] data = new byte[n];
//		for (int i = 0; i < n; i++) {
//			data[i] = Integer.valueOf(list[i]).byteValue();
//		}
		byte[] data = Base64.getDecoder().decode(userSessionToken);
		NTGObjectStreamReader reader = new NTGObjectStreamReader(data);

		return (CRMSessionInfo) reader.DCompressData();
	}

	public static Object getCurrentTenant() {
		CRMSessionInfo info = currentTenant.get();
		if (info == null) {
			if (Application.ApplicaitonIsIntializing != 2) {
				if (Application.ApplicaitonIsIntializing == 0) {
					TenantContext.setCurrentTenantByCompanyName(CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID , "main", "Application.class");
					Application.NTGInit();
				}
				return CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID;
			} else {
				//logger.info("War:No Session Info Is Found @" + NTGMessageOperation.GetCurrentTrace());
				return null;
			}
		} else {
			return info.companyName;
		}
	}

	public static CRMSessionInfo getCurrentTenantInfo() {
		CRMSessionInfo info = currentTenant.get();
		if (info == null) {
			if (Application.ApplicaitonIsIntializing != 2) {
				if (Application.ApplicaitonIsIntializing == 0) {
					TenantContext.setCurrentTenantByCompanyName(CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID , "main", "Application.class");
					Application.NTGInit();
				}
				return null;
			} else {
				//logger.info("War:No Session Info Is Found  @" + NTGMessageOperation.GetCurrentTrace());
				return null;
			}
		} else {
			return info;
		}
	}

	public static CRMSessionInfo getCurrentTenantInfo(boolean WithNoError) {
		return currentTenant.get();

	}

	public static void removeToken(String sessionToken) {
		UserSessionTokenCacher.remove(sessionToken);

	}

	public static void removeToken() {
		CRMSessionInfo info = currentTenant.get();
		if (info != null) {
			currentTenant.set(null);
			UserSessionTokenCacher.remove(info.getTaken());
		}
	}

	public static void setCompanyName(String companyName) {
		if (Utils.isEmpty(companyName)) {
			companyName = "NTG";
		}
		companyMap.put("companyName", companyName);
	}

	public static String getCompanyName() {
		return Utils.isNotEmpty(companyMap.get("companyName")) ? companyMap.get("companyName").toUpperCase() : "NTG";
	}
}
