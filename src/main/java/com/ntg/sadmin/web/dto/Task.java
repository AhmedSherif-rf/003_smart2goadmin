package com.ntg.sadmin.web.dto;

import java.io.Serializable;
import java.util.ArrayList;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class Task implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long Internal_ID;
	public long Plan_Internal_ID;

	public long TaskTypeID;
	public String TaskName;

	public long TaskProcessID;

	public long XPos;

	public long YPos;
	
	public String describtion;


	public ArrayList<TaskRouting> routing;



}
