package com.ntg.sadmin.web.dto;

import java.io.Serializable;

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
 * @version 3.0
 */
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class Msg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long RecID;
	public long FromUserID;
	public String MessageBody;
	public java.util.Date SendDate;
	public java.sql.Time SendTime;
	public long MsgStatus;
	public long ToUserID;
	public long OpID;
	public long InComRecID;
	public String FromName;
	public String ToName;

	public String fromImage;

	public long toEmployeeID;
	public long toGroupID;
	public long toPrivilegeID;
	public long toDeptID;
	public byte[] tag;

	public Msg() {
	}

}
