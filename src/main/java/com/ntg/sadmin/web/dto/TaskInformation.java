package com.ntg.sadmin.web.dto;

import javax.xml.bind.annotation.XmlRootElement;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

@XmlRootElement(name = "Task_Information")
public class TaskInformation implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskInformation() {
	}
 
	public ToDoListMainFields RowData;

}
