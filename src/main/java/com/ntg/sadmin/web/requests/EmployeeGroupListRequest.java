package com.ntg.sadmin.web.requests;

import java.io.Serializable;

import com.ntg.sadmin.web.dto.SessionInfo;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class EmployeeGroupListRequest implements Serializable {
	
	private static final long serialVersionUID = 2698612294100837834L;
	
	public SessionInfo LoginUserInfo;
	public long empId;
	public boolean AllGroups =false;
	
}
