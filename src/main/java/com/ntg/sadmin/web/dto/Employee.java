package com.ntg.sadmin.web.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class Employee {

	public long GroupID;
	public boolean IsMember;
	public Long UserID;
	public String email;
	
	public String MemberUserName;
	public String ManagerUserName;
	
	public String MemberEmail;
	public String MANAGER_EMAIL;
	
	public String NAME_FIRST;
	public String NAME_LAST;
	
	public String MemberPhoneNumber;
	public String ManagerPhoneNumber;
	
	public String name;
	public String ManagerName;
	
	public String EmployeeName;


	///sms attribute///
	public String sms_2fa_enabled;
	public String sms_2fa_code;
	public String sms_expire_time;

	public String getSms_2fa_enabled() {
		return sms_2fa_enabled;
	}

	public void setSms_2fa_enabled(String sms_2fa_enabled) {
		this.sms_2fa_enabled = sms_2fa_enabled;
	}

	public String getSms_2fa_code() {
		return sms_2fa_code;
	}

	public void setSms_2fa_code(String sms_2fa_code) {
		this.sms_2fa_code = sms_2fa_code;
	}

	public String getSms_expire_time() {
		return sms_expire_time;
	}

	public void setSms_expire_time(String sms_expire_time) {
		this.sms_expire_time = sms_expire_time;
	}

	public String getEmployeeName() {
		return EmployeeName;
	}
	public void setEmployeeName(String employeeName) {
		EmployeeName = employeeName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public long getGroupID() {
		return GroupID;
	}
	public void setGroupID(long groupID) {
		GroupID = groupID;
	}
	public boolean isIsMember() {
		return IsMember;
	}
	public void setIsMember(boolean isMember) {
		IsMember = isMember;
	}
	public Long getUserID() {
		return UserID;
	}
	public void setUserID(Long userID) {
		UserID = userID;
	}
	public String getMemberUserName() {
		return MemberUserName;
	}
	public void setMemberUserName(String memberUserName) {
		MemberUserName = memberUserName;
	}
	public String getManagerUserName() {
		return ManagerUserName;
	}
	public void setManagerUserName(String managerUserName) {
		ManagerUserName = managerUserName;
	}
	public String getMemberEmail() {
		return MemberEmail;
	}
	public void setMemberEmail(String memberEmail) {
		MemberEmail = memberEmail;
	}
	public String getMANAGER_EMAIL() {
		return MANAGER_EMAIL;
	}
	public void setMANAGER_EMAIL(String mANAGER_EMAIL) {
		MANAGER_EMAIL = mANAGER_EMAIL;
	}
	public String getNAME_FIRST() {
		return NAME_FIRST;
	}
	public void setNAME_FIRST(String nAME_FIRST) {
		NAME_FIRST = nAME_FIRST;
	}
	public String getNAME_LAST() {
		return NAME_LAST;
	}
	public void setNAME_LAST(String nAME_LAST) {
		NAME_LAST = nAME_LAST;
	}
	public String getMemberPhoneNumber() {
		return MemberPhoneNumber;
	}
	public void setMemberPhoneNumber(String memberPhoneNumber) {
		MemberPhoneNumber = memberPhoneNumber;
	}
	public String getManagerPhoneNumber() {
		return ManagerPhoneNumber;
	}
	public void setManagerPhoneNumber(String managerPhoneNumber) {
		ManagerPhoneNumber = managerPhoneNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getManagerName() {
		return ManagerName;
	}
	public void setManagerName(String managerName) {
		ManagerName = managerName;
	}
	

	


	
}
