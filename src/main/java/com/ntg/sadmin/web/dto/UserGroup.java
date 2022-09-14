package com.ntg.sadmin.web.dto;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class UserGroup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long RecID;

	public long GroupID;

	@JsonInclude(Include.NON_EMPTY)
	public String Group_Name;

	@XmlElement(name = "UserID")
	public long UserID;
	
	@XmlElement
	public long userPermissionLevel;
	@XmlElement
	public boolean isdefault;
	
	@XmlElement
	public String groupStatusName;

}
