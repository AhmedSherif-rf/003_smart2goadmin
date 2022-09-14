package com.ntg.sadmin.web.response;

import java.io.Serializable;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class GroupAllDetails implements Serializable {

    private Long groupId;
    private String name;
    private String email;
    private String description;
    private Long statusId;
    private String statusName;
    private Long groupPrimeId;
    private Long groupType;
    private Boolean isDefault;
    private Boolean isHaveAdminPrev;
    private GroupAllDetails parentGroup;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Long getGroupPrimeId() {
        return groupPrimeId;
    }

    public void setGroupPrimeId(Long groupPrimeId) {
        this.groupPrimeId = groupPrimeId;
    }

    public Long getGroupType() {
        return groupType;
    }

    public void setGroupType(Long groupType) {
        this.groupType = groupType;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getHaveAdminPrev() {
        return isHaveAdminPrev;
    }

    public void setHaveAdminPrev(Boolean haveAdminPrev) {
        isHaveAdminPrev = haveAdminPrev;
    }

    public GroupAllDetails getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(GroupAllDetails parentGroup) {
        this.parentGroup = parentGroup;
    }
}
