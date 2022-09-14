package com.ntg.sadmin.web.requests;

import com.ntg.sadmin.data.entities.Employee;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class AddEmployeeRequest {

	public Employee emp ;
	public String companyName;
	public Boolean canUpdateStatus;
}
