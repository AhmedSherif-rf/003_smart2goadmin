package com.ntg.sadmin.web.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class UserCompaniesList {

	public long Group_ID;
    public String Name;
    public long gruop_type;
    
	public long getGroup_ID() {
		return Group_ID;
	}
	public void setGroup_ID(long group_ID) {
		Group_ID = group_ID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public long getGruop_type() {
		return gruop_type;
	}
	public void setGruop_type(long gruop_type) {
		this.gruop_type = gruop_type;
	}
    
	
	


}
