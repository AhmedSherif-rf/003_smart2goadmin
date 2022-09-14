package com.ntg.sadmin.web.requests;

import com.ntg.sadmin.web.dto.SessionInfo;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class GetToDoListRequest  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GetToDoListRequest() {
	}
	public SessionInfo  LoginUserInfo;
	public String companyName;
}
