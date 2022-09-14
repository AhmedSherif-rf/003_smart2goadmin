package com.ntg.sadmin.web.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntg.sadmin.exceptions.NTGRestException;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class LoginUser {

	public LoginUser() {
	}

	@XmlElement
	public LoginUserInfo MainLoginInfo;

	@XmlElement
	public long ServerTimeZoneOffset;

	@XmlElement
	public ManagerInfo managerInfo;
	
	public String employeeId;

	@XmlElement
	public String DefaultCurrencySymbol;

	@XmlElement
	@JsonInclude(Include.NON_EMPTY)
	public NTGRestException restException;

	@JsonInclude(Include.NON_EMPTY)
	public List<UserGroup> UserGroups;

	@JsonInclude(Include.NON_EMPTY)
	public Boolean isHaveAdminPrev = false;

	public String UserSessionToken;
 
	@XmlElement
	public String Smart2GoSessionToken;

	@JsonInclude(Include.NON_EMPTY)public Boolean embeded;
	public void setEmbeded(Boolean embeded) 
	{this.embeded = embeded;}

	@XmlElement
	@JsonInclude(Include.NON_EMPTY)
	public boolean isSuperAdmin;
}
