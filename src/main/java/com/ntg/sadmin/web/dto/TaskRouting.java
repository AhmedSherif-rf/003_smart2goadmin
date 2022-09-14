package com.ntg.sadmin.web.dto;

import java.io.Serializable;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class TaskRouting implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TaskRouting(){
		
	}
	
	public long Task_Internal_ID;
	public long RouteTo_Task_Internal_ID;

}
