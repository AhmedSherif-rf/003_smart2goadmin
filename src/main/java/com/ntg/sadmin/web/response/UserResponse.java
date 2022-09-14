package com.ntg.sadmin.web.response;

import com.ntg.sadmin.web.dto.EmployeeInfo;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class UserResponse {

	public EmployeeInfo[] returnValue;
}
