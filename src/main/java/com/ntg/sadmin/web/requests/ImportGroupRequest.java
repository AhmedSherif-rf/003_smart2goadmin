package com.ntg.sadmin.web.requests;

import com.ntg.sadmin.web.dto.SessionInfo;
import com.ntg.sadmin.web.response.GroupAllDetails;

import java.io.Serializable;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class ImportGroupRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private SessionInfo loginUserInfo;
    private GroupAllDetails group;
    private String companyName;


    public SessionInfo getLoginUserInfo() {
        return loginUserInfo;
    }

    public void setLoginUserInfo(SessionInfo loginUserInfo) {
        this.loginUserInfo = loginUserInfo;
    }

    public GroupAllDetails getGroup() {
        return group;
    }

    public void setGroup(GroupAllDetails group) {
        this.group = group;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
