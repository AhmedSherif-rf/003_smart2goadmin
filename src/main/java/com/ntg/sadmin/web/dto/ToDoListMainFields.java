package com.ntg.sadmin.web.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class ToDoListMainFields {

	public String AssignDesc;

	public String FromDepName;

	public String ToDepName;

	public String ReqAction;
	public String taskName;
	public String customerName;
	public String projectNumber;
	public String requestNumber;
	public String circuitName;
	public byte[] FromPersoanSmallimage;
	public double PassTime;

	public String Priority;

	public long Priority_Color;

}
