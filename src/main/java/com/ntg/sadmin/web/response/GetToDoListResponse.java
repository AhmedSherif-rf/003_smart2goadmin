package com.ntg.sadmin.web.response;

import com.ntg.sadmin.web.dto.TaskInformation;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class GetToDoListResponse extends BaseResponse {

	public TaskInformation[] returnValue;

}
