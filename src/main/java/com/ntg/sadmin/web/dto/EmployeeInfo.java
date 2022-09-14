package com.ntg.sadmin.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntg.sadmin.utils.EncryptionUtils;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class EmployeeInfo {

//	public static char[] Enc = { 'N', 'T', 'G' };
	public String gender;
	public String Name;
	public String Email;
	public String Password;
	public String User_Name;	
	public String Name_First;
	public String Name_Last;
	public String Mobile_Phone;
 	public long Status_ID;
	public long loginType;
	public long Emp_ID;
	public String CORPORATE_ID;
	public long DeptID;
	public String locationName;
	public long PARENT_ID;
	public String statusName;
	public double HourCost;
	public long workinghours;
	public String WorkingDays;
	public Time startWorkingHour = new Time(90 * 1000 * 60 * 60);
	public Date Expire_Date;
	public String Skills;
	public String Prefered_Language="en";
	public long ContractTypeID=1;
	@JsonIgnore
	public byte[] SmallImage;

	public String otp;

	public Date otpValidUntil;
	public byte[] image;

	public boolean isRegistering;
	public Boolean svgFlag;
	
	public void setStartWorkingHelper(String startWorkingHelper) {
		if(startWorkingHelper != null){
		 	LocalTime localTime = LocalTime.parse(startWorkingHelper);
//			System.out.println("startWorkingHour : "+Time.valueOf(localTime));
			this.startWorkingHour = Time.valueOf(localTime);			
		}
	}

	public void setHelperImage(String helperImage) {
		if(helperImage != null){
			image = Base64.getDecoder().decode(helperImage);
			SmallImage = image;
		}
	}

	public void setHelperPassword(String helperPassword) {
		if (helperPassword == null || helperPassword.equals("")) {
			Password = ""  ;
		}
		int j = 0;
		char[] C = helperPassword.toCharArray();
		for (int i = 0; i < C.length; i++) {
			C[i] ^= EncryptionUtils.Enc[j];
			j++;
			if (j > 2) {
				j = 0;
			}
		}
//		System.out.println("setPassword  : "+String.valueOf(C));
		Password = String.valueOf(C);
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getUser_Name() {
		return User_Name;
	}

	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}

	public String getName_First() {
		return Name_First;
	}

	public void setName_First(String name_First) {
		Name_First = name_First;
	}

	public String getName_Last() {
		return Name_Last;
	}

	public void setName_Last(String name_Last) {
		Name_Last = name_Last;
	}

	public String getMobile_Phone() {
		return Mobile_Phone;
	}

	public void setMobile_Phone(String mobile_Phone) {
		Mobile_Phone = mobile_Phone;
	}

	public long getStatus_ID() {
		return Status_ID;
	}

	public void setStatus_ID(long status_ID) {
		Status_ID = status_ID;
	}

	public long getLoginType() {
		return loginType;
	}

	public void setLoginType(long loginType) {
		this.loginType = loginType;
	}

	public long getEmp_ID() {
		return Emp_ID;
	}

	public void setEmp_ID(long emp_ID) {
		Emp_ID = emp_ID;
	}

	public String getCORPORATE_ID() {
		return CORPORATE_ID;
	}

	public void setCORPORATE_ID(String cORPORATE_ID) {
		CORPORATE_ID = cORPORATE_ID;
	}

	public long getDeptID() {
		return DeptID;
	}

	public void setDeptID(long deptID) {
		DeptID = deptID;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public long getPARENT_ID() {
		return PARENT_ID;
	}

	public void setPARENT_ID(long pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public double getHourCost() {
		return HourCost;
	}

	public void setHourCost(double hourCost) {
		HourCost = hourCost;
	}

	public long getWorkinghours() {
		return workinghours;
	}

	public void setWorkinghours(long workinghours) {
		this.workinghours = workinghours;
	}

	public String getWorkingDays() {
		return WorkingDays;
	}

	public void setWorkingDays(String workingDays) {
		WorkingDays = workingDays;
	}

	public Time getStartWorkingHour() {
		return startWorkingHour;
	}

	public void setStartWorkingHour(Time startWorkingHour) {
		this.startWorkingHour = startWorkingHour;
	}

	public Date getExpire_Date() {
		return Expire_Date;
	}

	public void setExpire_Date(Date expire_Date) {
		Expire_Date = expire_Date;
	}

	public String getSkills() {
		return Skills;
	}

	public void setSkills(String skills) {
		Skills = skills;
	}

	public String getPrefered_Language() {
		return Prefered_Language;
	}

	public void setPrefered_Language(String prefered_Language) {
		Prefered_Language = prefered_Language;
	}

	public long getContractTypeID() {
		return ContractTypeID;
	}

	public void setContractTypeID(long contractTypeID) {
		ContractTypeID = contractTypeID;
	}

	public byte[] getSmallImage() {
		return SmallImage;
	}

	public void setSmallImage(byte[] smallImage) {
		SmallImage = smallImage;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean isRegistering() {
		return isRegistering;
	}

	public void setRegistering(boolean isRegistering) {
		this.isRegistering = isRegistering;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getOtpValidUntil() {
		return otpValidUntil;
	}

	public void setOtpValidUntil(Date otpValidUntil) {
		this.otpValidUntil = otpValidUntil;
	}
}
