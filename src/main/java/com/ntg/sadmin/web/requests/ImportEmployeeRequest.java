package com.ntg.sadmin.web.requests;

import com.ntg.sadmin.web.dto.SessionInfo;
import com.ntg.sadmin.web.response.EmployeeDetails;

import java.io.Serializable;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class ImportEmployeeRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private SessionInfo loginUserInfo;
    private EmployeeDetails employee;
    private String companyName;
    private Long numberOfUsers;

    public SessionInfo getLoginUserInfo() {
        return loginUserInfo;
    }

    public void setLoginUserInfo(SessionInfo loginUserInfo) {
        this.loginUserInfo = loginUserInfo;
    }

    public EmployeeDetails getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDetails employee) {
        this.employee = employee;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(Long numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}
