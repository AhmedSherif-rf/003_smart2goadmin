package com.ntg.sadmin.web.requests;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import com.ntg.sadmin.web.dto.SessionInfo;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class BaseRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(required = true, nillable = false)
	public SessionInfo LoginUserInfo;

	public BaseRequest() {
	}

	public BaseRequest(SessionInfo LoginUserInfo) {
		this.LoginUserInfo = LoginUserInfo;
	}

}
