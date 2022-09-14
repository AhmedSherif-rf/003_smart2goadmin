package com.ntg.sadmin.web.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class Corporation {

	public String Name;
	public String Email;
	public String Description;
	public Long Status_ID;
	public Long Group_ID;// RecId
	public Long gruop_type;// Type
	public Long PARENT_GROUP_ID;// Parent_Id
	//added by Abdulrahman For SAdmin you can comment if it cause any problem with itts Integeration
	public Boolean isHaveAdminPrev;
	public String ccList;
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Long getRecId() {
		return Group_ID;
	}

	public void setRecId(Long recId) {
		Group_ID = recId;
	}

	public Long getType() {
		return gruop_type;
	}

	public void setType(Long type) {
		this.gruop_type = type;
	}

	public Long getPARENT_GROUP_ID() {
		return PARENT_GROUP_ID;
	}

	public void setPARENT_GROUP_ID(Long pARENT_GROUP_ID) {
		PARENT_GROUP_ID = pARENT_GROUP_ID;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}
}
