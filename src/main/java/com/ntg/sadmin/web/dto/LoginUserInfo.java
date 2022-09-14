package com.ntg.sadmin.web.dto;

import java.sql.Time;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * <p>
 * Title: Network TeleCome System
 * </p>
 * <p>
 * Description: NTS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004, NTG-Clarity Network All rights reserved
 * </p>
 * <p>
 * Company: NTG Clarity Egypt Office Co.
 * </p>
 *
 * @author NTG
 * @sion 3.0
 */
@XmlAccessorType(XmlAccessType.NONE)
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class LoginUserInfo implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude(Include.NON_EMPTY)
    @XmlElement(name = "SessionInfo")
    public SessionInfo SessionInfo;

    @XmlElement
    public String fullName;
    @XmlElement
    public String Mail;

    @XmlElement
    public java.util.Date expiredate;

    @XmlElement
    public String BranchName;

    @XmlElement
    public String WorkingDays;

    @XmlElement
    public double hourcost;
    @XmlElement
    public double WorkingHours;

    @XmlElement
    public Time StartWorkingHour;


    @XmlElement
    public Boolean isHaveAdminPrev = false;



    @XmlElement
    public byte[] Image;

    @XmlElement
    public long EmpStatus;

    public Long userType;
}
