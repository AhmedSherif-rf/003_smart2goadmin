package com.ntg.sadmin.web.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class DataObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement
	public Long recid = 0L;
	@XmlElement
	public String name;

}
