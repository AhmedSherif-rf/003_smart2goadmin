package com.ntg.sadmin.web.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class Group {

	public String Name;
	public String Email;
	public Long Group_ID;
	public Long type;
	public Long PARENT_GROUP_ID;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Long getGroup_ID() {
		return Group_ID;
	}

	public void setGroup_ID(Long group_ID) {
		Group_ID = group_ID;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
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

}
