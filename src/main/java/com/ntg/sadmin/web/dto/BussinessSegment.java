package com.ntg.sadmin.web.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonFormat;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

@XmlAccessorType(XmlAccessType.NONE)
public class BussinessSegment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement
	public long RecID;
	
	@XmlElement
	public String Name;
	
	@XmlElement
	public String Description;
	
	@XmlElement
	public String WorkingDays;
	
	@XmlElement
	@JsonFormat(pattern = "HH:mm")
	public Date StartWorkingHour;
	
	@XmlElement
	@JsonFormat(pattern = "HH:mm")
	public Date EndWorkingHour;
	
	@XmlElement
	public long WorkingHours;

	public long getRecID() {
		return RecID;
	}

	public void setRecID(long recID) {
		RecID = recID;
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

	public Date getStartWorkingHour() {
		return StartWorkingHour;
	}

	public void setStartWorkingHour(Date startWorkingHour) {
		StartWorkingHour = startWorkingHour;
	}

	public Date getEndWorkingHour() {
		return EndWorkingHour;
	}

	public void setEndWorkingHour(Date endWorkingHour) {
		EndWorkingHour = endWorkingHour;
	}

	public long getWorkingHours() {
		return WorkingHours;
	}

	public void setWorkingHours(long workingHours) {
		WorkingHours = workingHours;
	}

}
