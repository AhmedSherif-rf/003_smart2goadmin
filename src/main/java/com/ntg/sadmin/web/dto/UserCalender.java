package com.ntg.sadmin.web.dto;

 
import java.io.Serializable;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

@XmlRootElement
public class UserCalender  implements Serializable {
	// type 0 means Start or resume && type 1 means Pause , 2 Mean Done Done
	// Task
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int StepNo;
	public final static int Start_Resume_Task_Action = 0;
	public final static int PuaseTask_Action = 1;
	public final static int DoneTask_Action = 2;
	
	public long RecID;

	public Timestamp StartDate;

	public Timestamp endDate;
	
	public String description;
	
	public long type;
	
	public long program;
	
	public long customerID;
	
	public long empid;
	
	public long Incom_RecID;
	
	public long OpID;
	
	public String TaskName;
	
	public double reported_hours;
	
	public double reported_cost;
	
	public long ToDoListTaskID;
	
	public boolean isNewCalendar;

	public String employeeName;

	 
	public long Work_From_ID = 1;
	
	public long Reported_Percentage_Done_Work;
 

}
