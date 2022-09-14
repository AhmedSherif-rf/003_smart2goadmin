package com.ntg.sadmin.web.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntg.sadmin.web.dto.SessionInfo;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class LoginUserRequest {
	public SessionInfo LoginUserInfo;

	@JsonInclude(Include.NON_EMPTY)
	public String Password;
	@JsonInclude(Include.NON_EMPTY)
	public boolean isAdmin;
	@JsonInclude(Include.NON_EMPTY)
	public String LoginMachineName;
	@JsonInclude(Include.NON_EMPTY)
	public String LoginIPAddress;
	@JsonInclude(Include.NON_EMPTY)
	public String AppPort;
	@JsonInclude(Include.NON_EMPTY)
	public boolean enableRecaptcha;
	@JsonInclude(Include.NON_EMPTY)
	public String companyName;

	@JsonInclude(Include.NON_EMPTY)
	public String userAgent;

	@JsonInclude(Include.NON_EMPTY)
	public String userIp;

	@JsonInclude(Include.NON_EMPTY)
	public String userPlatform;
	@Override
	public String toString() {
		return "LoginUserRequest [LoginUserInfo=" + LoginUserInfo + ", isAdmin=" + isAdmin + ", LoginMachineName="
				+ LoginMachineName + ", LoginIPAddress=" + LoginIPAddress + ", AppPort=" + AppPort + "]";
	}

}


