package com.ntg.sadmin.web.requests;

import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.entities.Group;
import com.ntg.sadmin.web.dto.SessionInfo;

import java.io.Serializable;
import java.util.List;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class TenantGroupsUserRequest implements Serializable {

    private SessionInfo LoginUserInfo;
    private Employee emp;
    private List<Group> groups;
    private String companyName;
    private String oldTenant;

    public SessionInfo getLoginUserInfo() {
        return LoginUserInfo;
    }

    public void setLoginUserInfo(SessionInfo loginUserInfo) {
        LoginUserInfo = loginUserInfo;
    }

    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOldTenant() {
        return oldTenant;
    }

    public void setOldTenant(String oldTenant) {
        this.oldTenant = oldTenant;
    }
}
