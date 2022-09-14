package com.ntg.sadmin.web.dto;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.ntg.sadmin.common.NTGObjectStream;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class CRMSessionInfo implements java.io.Serializable {			


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CRMSessionInfo() {
	}
	
	public CRMSessionInfo(LoginUser loginUser) {
		if (loginUser.UserGroups != null && loginUser.UserGroups.size() > 0) {
			UserGroups = String.valueOf(loginUser.UserGroups.get(0).GroupID);

			for (int i = 1, n = loginUser.UserGroups.size(); i < n; i++) {
				UserGroups += "," + loginUser.UserGroups.get(i).GroupID;
				GroupName += ",'" + loginUser.UserGroups.get(i).Group_Name + "'";
				 
			}
		}

		hourcost = loginUser.MainLoginInfo.hourcost;
		WorkingHours = loginUser.MainLoginInfo.WorkingHours;
		StartWorkingHour = loginUser.MainLoginInfo.StartWorkingHour;
		loginUserName = loginUser.MainLoginInfo.SessionInfo.loginUserName;
		sessionID = loginUser.MainLoginInfo.SessionInfo.sessionID;
		companyName = loginUser.MainLoginInfo.SessionInfo.companyName;
		userID = loginUser.MainLoginInfo.SessionInfo.userID;
		isHaveAdminPrev = loginUser.isHaveAdminPrev;
		FullName = loginUser.MainLoginInfo.fullName;
	

		// clear data which should not send to the client
//		loginUser.UserGroups = null;
		loginUser.isHaveAdminPrev = null;
		loginUser.MainLoginInfo.SessionInfo = null;
	}

	public CRMSessionInfo(String Taken) {

	}

 	public String UserGroups;
	public double hourcost;
	public double WorkingHours;
	public Time StartWorkingHour;
	public String loginUserName;
	public String sessionID;
	public String companyName;
	public String userID;
	public boolean isHaveAdminPrev;
	public String FullName;
	private String Token = null;
 	public DataObject [] groupList;
 	public String GroupName = null;

	public String getTaken() {
		if (Token != null) {
			return Token;
		}
		NTGObjectStream NTG_OBJECT_STREAM = new NTGObjectStream();
		try {
			Token = NTG_OBJECT_STREAM.getData(this);
			return Token;
		} catch (Exception e) {
			Token = null;
			return "error:" + e.getMessage();

		}
	}

	public List<Long> getGroupsIDList() {
		List<Long> list = new ArrayList<Long>();
		if (UserGroups != null) {
			String[] ids = UserGroups.split(",");
			for (String id : ids) {
				list.add(Long.valueOf(id));
			}
		}
		return list;
	}

	SessionInfo sessionInfo = null;

	public SessionInfo getSessionInfo() {
		if (sessionInfo == null) {
			sessionInfo = new SessionInfo();
			sessionInfo.loginUserName = this.loginUserName;
			sessionInfo.sessionID = this.sessionID;
			sessionInfo.companyName = this.companyName;
			sessionInfo.userID = this.userID;
			sessionInfo.groupList = this.groupList;

		}
		return sessionInfo;
	}

	public String getUserGroups() {
		return UserGroups;
	}

	public double getHourcost() {
		return hourcost;
	}

	public double getWorkingHours() {
		return WorkingHours;
	}

	public Time getStartWorkingHour() {
		return StartWorkingHour;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getUserID() {
		return userID;
	}

	public boolean getIsHaveAdminPrev() {
		return isHaveAdminPrev;
	}

	public String getFullName() {
		return FullName;
	}

	public long GetEmployeeID(){
		return (this.userID == null)? -1 : Long.valueOf(this.userID);
	}

}
