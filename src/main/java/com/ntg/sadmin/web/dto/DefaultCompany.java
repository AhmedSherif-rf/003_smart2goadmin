package com.ntg.sadmin.web.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

@XmlAccessorType(XmlAccessType.NONE)
public class DefaultCompany implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement
	public long Group_ID;
	
	@XmlElement
	public String Name;
	
	@XmlElement
	public String Description;

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

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}
