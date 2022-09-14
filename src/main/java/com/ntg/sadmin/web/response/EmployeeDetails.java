package com.ntg.sadmin.web.response;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDetails {

    private String name;
    private String email;
    private String password;
    private String skills;
    private String userName;
    private String firstName;
    private String lastName;
    private String mobilePhone;
    private Long statusId;
    private Long loginType;
    private String corporateId;
    private Long deptId;
    private String locationName;
    private EmployeeDetails parentEmployee;
    private String statusName;
    private Double hourCost;
    private Long workingHours;
    private String workingDays;
    private Time startWorkingHour;
    private Date expireDate;
    private String prefereLanguage;
    private String gender;
    private Long contractTypeId;
    private String smallImage;
    private String image;
    private List<GroupAllDetails> userGroups;
    private Long userType;
    private Date accountTemporaryLockedStartTime;
    private Long temporaryLockCounter;
    private Long failedLoginCounter;
    private Boolean isSuperAdmin;
    private String sms2faEnabled;
    private String sms2faCode;
    private Long smsExpireTime;
    private Long Sms_expire_code_Number;

    public Long getSms_expire_code_Number() {
        return Sms_expire_code_Number;
    }

    public void setSms_expire_code_Number(Long sms_expire_code_Number) {
        Sms_expire_code_Number = sms_expire_code_Number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getLoginType() {
        return loginType;
    }

    public void setLoginType(Long loginType) {
        this.loginType = loginType;
    }

    public String getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(String corporateId) {
        this.corporateId = corporateId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public EmployeeDetails getParentEmployee() {
        return parentEmployee;
    }

    public void setParentEmployee(EmployeeDetails parentEmployee) {
        this.parentEmployee = parentEmployee;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Double getHourCost() {
        return hourCost;
    }

    public void setHourCost(Double hourCost) {
        this.hourCost = hourCost;
    }

    public Long getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Long workingHours) {
        this.workingHours = workingHours;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public Time getStartWorkingHour() {
        return startWorkingHour;
    }

    public void setStartWorkingHour(Time startWorkingHour) {
        this.startWorkingHour = startWorkingHour;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getPrefereLanguage() {
        return prefereLanguage;
    }

    public void setPrefereLanguage(String prefereLanguage) {
        this.prefereLanguage = prefereLanguage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getContractTypeId() {
        return contractTypeId;
    }

    public void setContractTypeId(Long contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<GroupAllDetails> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<GroupAllDetails> userGroups) {
        this.userGroups = userGroups;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public Date getAccountTemporaryLockedStartTime() {
        return accountTemporaryLockedStartTime;
    }

    public void setAccountTemporaryLockedStartTime(Date accountTemporaryLockedStartTime) {
        this.accountTemporaryLockedStartTime = accountTemporaryLockedStartTime;
    }

    public Long getTemporaryLockCounter() {
        return temporaryLockCounter;
    }

    public void setTemporaryLockCounter(Long temporaryLockCounter) {
        this.temporaryLockCounter = temporaryLockCounter;
    }

    public Long getFailedLoginCounter() {
        return failedLoginCounter;
    }

    public void setFailedLoginCounter(Long failedLoginCounter) {
        this.failedLoginCounter = failedLoginCounter;
    }

    public Boolean getSuperAdmin() {
        return (isSuperAdmin == null) ? false : isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public String getSms2faEnabled() {
        return sms2faEnabled;
    }

    public void setSms2faEnabled(String sms2faEnabled) {
        this.sms2faEnabled = sms2faEnabled;
    }

    public String getSms2faCode() {
        return sms2faCode;
    }

    public void setSms2faCode(String sms2faCode) {
        this.sms2faCode = sms2faCode;
    }

    public Long getSmsExpireTime() {
        return smsExpireTime;
    }

    public void setSmsExpireTime(Long smsExpireTime) {
        this.smsExpireTime = smsExpireTime;
    }

}
