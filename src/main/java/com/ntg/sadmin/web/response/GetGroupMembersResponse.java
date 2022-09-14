package com.ntg.sadmin.web.response;

import com.ntg.sadmin.web.dto.Employee;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class GetGroupMembersResponse extends BaseResponse {

	public Employee[] returnValue;

	public GetGroupMembersResponse() {
		super();
	}

	public GetGroupMembersResponse(Employee[] returnValue) {
		super();
		this.returnValue = returnValue;
	}

}
