package com.ntg.sadmin.web.requests;

import java.io.Serializable;

import com.ntg.sadmin.web.dto.SessionInfo;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class DeleteUserRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteUserRequest(){
		
	}
	public SessionInfo LoginUserInfo;

	public long RecID;

}
