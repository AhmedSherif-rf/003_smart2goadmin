package com.ntg.sadmin.web.requests;

import com.ntg.sadmin.web.dto.SessionInfo;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class UserRequest {

	public void setstatusID(long[] l) {
		if (l != null) {
			this.Filter = "A.StatusID in (" + l[0];
			for (int i = 1; i < l.length; i++) {
				this.Filter += "," + l[i];
			}
			this.Filter += ")";
		}
	}

	public SessionInfo LoginUserInfo;
	public String Filter;
	public int start;
	public int count;

}
