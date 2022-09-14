package com.ntg.sadmin.web.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class GroupOperation {

	public String PrevID;
	public String OperationID;
}
