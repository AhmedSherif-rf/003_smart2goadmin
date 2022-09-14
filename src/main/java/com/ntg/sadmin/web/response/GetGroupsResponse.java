package com.ntg.sadmin.web.response;

import com.ntg.sadmin.web.dto.Group;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class GetGroupsResponse extends BaseResponse {

	public Group[] returnValue;

	public GetGroupsResponse() {
		super();
	}

	public GetGroupsResponse(Group[] returnValue) {
		super();
		this.returnValue = returnValue;
	}

}
