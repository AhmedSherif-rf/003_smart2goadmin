package com.ntg.sadmin.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntg.sadmin.constants.CommonConstants;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.sql.Time;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Entity
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "comp_employee")
public class Employee extends AuditEntity<Object> {
	@Id
	@SequenceGenerator(allocationSize = 1, name = "comp_employee_s", sequenceName = "comp_employee_s", initialValue = 1000)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comp_employee_s")
	@Column(name = "recid", nullable = false)
	@XmlElement(name = "Emp_ID")
	public Long Emp_ID;

	@Column(name = "name")
	@XmlElement(name = "Name")
	public String Name;

	@Column(name = "email")
	@XmlElement(name = "Email")
	public String Email;

	@Column(name = "password", updatable = false)
	@XmlElement(name = "Password")
	public String Password;

	@Column(name = "Skills")
	@XmlElement(name = "Skills")
	public String Skills;

	@Column(name = "user_name")
	@XmlElement(name = "User_Name")
	public String User_Name;

	@Column(name = "first_name")
	@XmlElement(name = "Name_First")
	public String Name_First;

	@XmlElement(name = "Name_Last")
	@Column(name = "last_name")
	public String Name_Last;

	@XmlElement(name = "Mobile_Phone")
	@Column(name = "mobile_phone")
	public String Mobile_Phone;

	@Column(name = "STATUSID", nullable = true)
	@XmlElement(name = "Status_ID")
	public Long Status_ID;

	@Column(name = "login_type", nullable = true)
	public Long loginType;

	@Column(name = "CORPORATE_ID")
	@XmlElement(name = "CORPORATE_ID")
	public String CORPORATE_ID;

	@Column(name = "DeptID", nullable = true)
	@XmlElement(name = "DeptID")
	public Long DeptID;

	@Column(name = "location_Name")
	public String locationName;

	@Column(name = "PARENT_ID", nullable = true)
	@XmlElement(name = "PARENT_ID")
	public Long PARENT_ID;

	@Column(name = "STATUS_NAME")
	public String statusName;

	@Column(name = "hourcost", nullable = true)
	@XmlElement(name = "HourCost")
	public Double HourCost;

	@Column(name = "workinghours", nullable = true)
	public Long workinghours;

	@Column(name = "WORKINGDAYS")
	public String WorkingDays;

	@Column(name = "STARTWORKINGHOUR")
	public Time startWorkingHour = new Time(90 * 1000 * 60 * 60);

	@Column(name = "EXPIREDATE")
	@XmlElement(name = "Expire_Date")
	public Date Expire_Date;

	@Column(name = "Prefered_Language")
	@XmlElement(name = "Prefered_Language")
	public String Prefered_Language;

	@Column(name = "gender")
	public String gender;

    @Column(name = "otp")
    public String otp;

    @Column(name = "otp_valid_until")
    public Date otpValidUntil;

    @Column(name = "Contract_TypeID", nullable = true)
    @XmlElement(name = "CONTRACTTYPEID")
    public Long ContractTypeID = 1L;

	@Column(name = "SMALLIMAGE")
	@XmlElement(name = "SmallImage")
	@JsonIgnore
	public byte[] SmallImage;

	@Column(name = "image")
	public byte[] image;

	@Column(name = "svg_flag")
	@XmlElement(name = "svgFlag")
	public Boolean svgFlag;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, mappedBy = "groupUsers")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<Group> userGroups;

	public void setImage(String image) {
		if (image != null) {
			this.image = Base64.getDecoder().decode(image);
			SmallImage = this.image;
		}
	}

	@Column(name = "USER_TYPE")
	private Long userType = CommonConstants.USER_TYPE_NORMAL;

	@Column(name = "ACC_TEMP_LOCKED_START_TIME")
	private Date accountTemporaryLockedStartTime;

	@Column(name = "TEMPORARY_LOCKS_COUNTER")
	private Long temporaryLockCounter = 0L;

	@Column(name = "FAILED_LOGIN_COUNTER")
	private Long failedLoginCounter = 0L;

	@Column(name = "is_super_admin")
	@ColumnDefault("'0'")
	private Boolean isSuperAdmin;

	@Column(name = "is_2fa_enabled")
	private String sms_2fa_enabled;

	@Column(name = "is_2fa_code", nullable = true)
	private String sms_2fa_code;

	@Column(name = "is_expire_time", nullable = true)
	private Long sms_expire_time;

	@Column(name = "is_expire_code_Number", nullable = true)
	private Long sms_expire_code_Number = 0l;

	public Long getSms_expire_code_Number() {
		return sms_expire_code_Number == null ? 0L : sms_expire_code_Number;
	}

	public void setSms_expire_code_Number(Long sms_expire_code_Number) {
		this.sms_expire_code_Number = sms_expire_code_Number;
	}

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

	public long getSms_expire_time() {
		return (sms_expire_time == null) ? 0 : sms_expire_time;
	}

	public void setSms_expire_time(Long sms_expire_time) {
		this.sms_expire_time = sms_expire_time;
	}

	public void setAccountTemporaryLockedStartTime(Date accountTemporaryLockedStartTime) {
		this.accountTemporaryLockedStartTime = accountTemporaryLockedStartTime;
	}

	public Date getAccountTemporaryLockedStartTime() {
		return accountTemporaryLockedStartTime;
	}

	public Long getTemporaryLockCounter() {
		return temporaryLockCounter == null ? 0L : temporaryLockCounter;
	}

	public void setTemporaryLockCounter(Long temporaryLockCounter) {
		this.temporaryLockCounter = temporaryLockCounter;
	}

	public Long getFailedLoginCounter() {
		return failedLoginCounter == null ? 0L : failedLoginCounter;
	}

	public void setFailedLoginCounter(Long failedLoginCounter) {
		this.failedLoginCounter = failedLoginCounter;
	}

	public Long getUserType() {
		return userType == null ? 0L : userType;
	}

	public void setUserType(Long userType) {
		this.userType = userType;
	}

	public Long getEmp_ID() {
		return Emp_ID;
	}

	public void setEmp_ID(Long emp_ID) {
		Emp_ID = emp_ID;
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

	public String getSkills() {
		return Skills;
	}

	public void setSkills(String skills) {
		Skills = skills;
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

	public Long getStatus_ID() {
		return Status_ID;
	}

	public void setStatus_ID(Long status_ID) {
		Status_ID = status_ID;
	}

	public Long getLoginType() {
		return loginType;
	}

	public void setLoginType(Long loginType) {
		this.loginType = loginType;
	}

	public String getCORPORATE_ID() {
		return CORPORATE_ID;
	}

	public void setCORPORATE_ID(String cORPORATE_ID) {
		CORPORATE_ID = cORPORATE_ID;
	}

	public Long getDeptID() {
		return DeptID;
	}

	public void setDeptID(Long deptID) {
		DeptID = deptID;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Long getPARENT_ID() {
		return PARENT_ID;
	}

	public void setPARENT_ID(Long pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Double getHourCost() {
		return HourCost;
	}

	public void setHourCost(Double hourCost) {
		HourCost = hourCost;
	}

	public Long getWorkinghours() {
		return workinghours;
	}

	public void setWorkinghours(Long workinghours) {
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

	public String getPrefered_Language() {
		return Prefered_Language;
	}

	public void setPrefered_Language(String prefered_Language) {
		Prefered_Language = prefered_Language;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getContractTypeID() {
		return ContractTypeID;
	}

	public void setContractTypeID(Long contractTypeID) {
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

	public List<Group> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<Group> userGroups) {
		this.userGroups = userGroups;
	}

	public Boolean getIsSuperAdmin() {
		return (isSuperAdmin == null) ? false : isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean superAdmin) {
		isSuperAdmin = superAdmin;
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
