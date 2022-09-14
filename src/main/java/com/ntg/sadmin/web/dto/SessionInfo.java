package com.ntg.sadmin.web.dto;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class SessionInfo {

    public String loginUserName;
    public String sessionID;
    public String companyName;
    public String transactionID;
    public String userID;
    public String companyId;
    public DataObject[] groupList;

}
