package com.ntg.sadmin.web.dto;

import java.io.Serializable;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class GroupInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean IsDefualt;
    public Long EmpNum;
    public Object groupUsers;
    public Object groupPorjects;
    public Long Group_ID;
    public String Name;
    public String Description;
    public Long Status_ID;
    public String Email;
    public Long GroupPrimeID;
	public GroupOperation PrevList;
	public long PARENT_GROUP_ID;
	public long gruop_type;
	public String statusName ;
	
	public void setGroupTypeHelper(long groupTypeHelper) {
		if(groupTypeHelper == -1){
			this.gruop_type = 3;			
		}
		else {
			this.gruop_type=groupTypeHelper;
		}
	}

}
