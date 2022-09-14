package com.ntg.sadmin.web.controllers;

import com.fasterxml.jackson.databind.node.TextNode;
import com.ntg.sadmin.data.entities.Group;
import com.ntg.sadmin.web.ExportImport.ExportImportService;
import com.ntg.sadmin.web.dto.UserGroup;
import com.ntg.sadmin.web.requests.*;
import com.ntg.sadmin.web.response.StringResponse;
import com.ntg.sadmin.web.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author abdelrahman
 */
@RestController
@RequestMapping(value = "/ITTS_UserMgm_WS/rest/UserManagment/APIs", produces = "application/json")

public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private ExportImportService exportImportService;

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/CRMAddEmployee", method = RequestMethod.POST)
    public ResponseEntity<?> CRMAddEmployee(@RequestBody AddEmployeeRequest req) {
        return new ResponseEntity<>(userManagementService.CRMAddEmployee(req, true), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     */
    @RequestMapping(value = "/DeleteUser", method = RequestMethod.POST)
    public ResponseEntity<?> DeleteUser(@RequestBody DeleteUserRequest req) {
        return new ResponseEntity<>(userManagementService.DeleteUser(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     */
    @RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
    public ResponseEntity<?> updateEmployee(@RequestBody AddEmployeeRequest req) {
        return new ResponseEntity<>(userManagementService.updateEmployee(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createGroup", method = RequestMethod.POST)
    public ResponseEntity<?> createGroup(@RequestBody com.ntg.sadmin.web.requests.AddGroupRequest req) throws Exception {
        return new ResponseEntity<>(userManagementService.createGroup(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/EditGroup", method = RequestMethod.POST)
    public ResponseEntity<?> EditGroup(@RequestBody com.ntg.sadmin.web.requests.AddGroupRequest req) throws Exception {
        return new ResponseEntity<>(userManagementService.EditGroup(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetGroupMember", method = RequestMethod.POST)
    public ResponseEntity<?> GetGroupMember(@RequestBody GetGroupMembersRequest req) {
        return new ResponseEntity<>(userManagementService.GetGroupMember(req), HttpStatus.OK);
    }

    //Added by Asmaa to get list of employees in list of groups
    @RequestMapping(value = "/GetGroupsMembers", method = RequestMethod.POST)
    public ResponseEntity<?> GetGroupsMembers(@RequestBody UserGroup[] req) {
        return new ResponseEntity<>(userManagementService.GetGroupsMembers(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetEmployeeFullInformation", method = RequestMethod.POST)
    public ResponseEntity<?> GetEmployeeFullInformation(@RequestBody EmployeeFullInfoRequest req) {
        return new ResponseEntity<>(userManagementService.GetEmployeeFullInformation(req), HttpStatus.OK);
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getGroupList", method = RequestMethod.POST)
    public ResponseEntity<?> getGroupList() {
        return new ResponseEntity<>(userManagementService.getGroupList(), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetEmployeeListInfo", method = RequestMethod.POST)
    public ResponseEntity<?> GetEmployeeListInfo(@RequestBody UserRequest req) {
        return new ResponseEntity<>(userManagementService.GetEmployeeListInfo(req, false), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     */
    @RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
    public ResponseEntity<?> deleteGroup(@RequestBody DeleteGroupRequest req) throws Exception {
        return new ResponseEntity<>(userManagementService.deleteGroup(req), HttpStatus.OK);

    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetAllGroupsLightInfo", method = RequestMethod.POST)
    public ResponseEntity<?> GetAllGroupsLightInfo(@RequestBody BaseRequest req) {
        return new ResponseEntity<>(userManagementService.GetAllGroupsLightInfo(req.LoginUserInfo), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/AddUserToGroup", method = RequestMethod.POST)
    public ResponseEntity<?> AddUserToGroup(@RequestBody GroupMembersRequest req) {
        return new ResponseEntity<>(userManagementService.AddUserToGroup(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/RemoveUserFromGroup", method = RequestMethod.POST)
    public ResponseEntity<?> RemoveUserFromGroup(@RequestBody GroupMembersRequest req) {
        return new ResponseEntity<>(userManagementService.RemoveUserFromGroup(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetMembersByGroupName", method = RequestMethod.POST)
    public ResponseEntity<?> GetMembersByGroupName(@RequestBody GetGroupMembersRequest req) {
        return new ResponseEntity<>(userManagementService.GetMembersByGroupName(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getEmployeeGroupsList", method = RequestMethod.POST)
    public ResponseEntity<?> getEmployeeGroupsList(@RequestBody EmployeeGroupListRequest req) {
        return new ResponseEntity<>(userManagementService.getEmployeeGroupsList(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getGroupHierarchy", method = RequestMethod.POST)
    public ResponseEntity<?> getGroupHierarchy(@RequestBody GroupDetailsRequest req) {
        return new ResponseEntity<>(userManagementService.getGroupHierarchy(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getGroupHierarchyDetails", method = RequestMethod.POST)
    public ResponseEntity<?> getGroupHierarchyDetails(@RequestBody GroupDetailsRequest req) {
        Group data = userManagementService.getGroupHierarchyDetails(req);
        // data.groupUsers = null;
        if (data.parentGroup != null) {
            data.parentGroup.groupUsers = null;
        }

        data.groupUsers = null;

        ResponseEntity<Object> re = new ResponseEntity<>(data, HttpStatus.OK);

        return re;
    }

    // added for work flow
    @RequestMapping(value = "/wf_GetEmail", method = RequestMethod.POST)
    public ResponseEntity<?> wf_GetEmail(@RequestBody Long Emp_id) {
        return new ResponseEntity<>(new StringResponse(userManagementService.wf_GetEmail(Emp_id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/wf_GetGroupEmail", method = RequestMethod.POST)
    public ResponseEntity<?> wf_GetGroupEmail(@RequestBody Long group_id) {
        return new ResponseEntity<>(new StringResponse(userManagementService.wf_GetGroupEmail(group_id)), HttpStatus.OK);
    }

    @PostMapping(value = "/wf_GetGroupCCList")
    public ResponseEntity<StringResponse> wfGetGroupCCList(@RequestBody Long groupId) {
        return new ResponseEntity<>(new StringResponse(userManagementService.wfGetGroupCCList(groupId)), HttpStatus.OK);
    }

    /**
     * @param emp_id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getMentionedEmployee", method = RequestMethod.POST)
    public ResponseEntity<?> getMentionedEmployee(@RequestBody Long emp_id) {
        return new ResponseEntity<>(userManagementService.getMentionedEmployee(emp_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/getEmployeeByMail", method = RequestMethod.POST)
    public ResponseEntity<?> getEmployeeByMail(@RequestBody TextNode mail) {
        return new ResponseEntity<>(userManagementService.getEmployeeByMail(mail.asText()), HttpStatus.OK);
    }


    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetUsers", method = RequestMethod.POST)
    public ResponseEntity<?> GetUsers() {
        return new ResponseEntity<>(userManagementService.GetEmployeeListInfo(null, false), HttpStatus.OK);
    }

    // added By Mahmoud to get Manager Employees
    @RequestMapping(value = "/GetManagerEmployees", method = RequestMethod.POST)
    public ResponseEntity<?> GetManagerEmployees(@RequestBody Long managerId) {
        return new ResponseEntity<>(userManagementService.GetManagerEmployees(managerId), HttpStatus.OK);
    }

    //added by Asmaa to get Employees of list of Managers
    @RequestMapping(value = "/GetManagersEmployees", method = RequestMethod.POST)
    public ResponseEntity<?> GetManagersEmployees(@RequestBody ArrayList<Long> managerIds) {
        return new ResponseEntity<>(userManagementService.GetManagersEmployees(managerIds), HttpStatus.OK);
    }

    // added by Abdulrahman Updated By Mahmoud
    // to retrieve manager using empId and manager level
    @RequestMapping(value = "/GetEmployeeMangers", method = RequestMethod.POST)
    public ResponseEntity<?> GetEmployeeMangers(@RequestBody Map<String, Long> managerMap) {
        Long empId = managerMap.get("empId");
        Long managerLevel = managerMap.get("managerLevel");
        return new ResponseEntity<>(userManagementService.GetEmployeeMangers(empId, managerLevel), HttpStatus.OK);
    }

    @RequestMapping(value = "/wf_GetEmployeesEmails", method = RequestMethod.POST)
    public ResponseEntity<?> wf_GetEmail(@RequestBody List<Long> Employees_id) {
        return new ResponseEntity<>(userManagementService.wf_GetEmployeesEmails(Employees_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/updateEmployeeParentId", method = RequestMethod.POST)
    public ResponseEntity<?> updateEmployeeParentId(@RequestBody Map request) {


        userManagementService.updateEmployeeParentId(request);
        return new ResponseEntity<>("updated", HttpStatus.OK);
    }


    @RequestMapping(value = "/addTenantUser", method = RequestMethod.POST)
    public ResponseEntity<?> addTenantUser(@RequestBody TenantGroupsUserRequest req) throws Exception {
        return new ResponseEntity<>(userManagementService.addTenantUser(req, Boolean.TRUE), HttpStatus.OK);
    }

    @RequestMapping(value = "/getEmployeeByUsername", method = RequestMethod.POST)
    public ResponseEntity<?> getEmployeeByUsername(@RequestBody HashMap<String, String> targetUser) throws Exception {
        return new ResponseEntity<>(userManagementService.getEmployeeByUsername(targetUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/GetLoginEmployeeAndHisChildrenListInfo", method = RequestMethod.POST)
    public ResponseEntity<?> GetLoginEmployeeAndHisChildrenListInfo(@RequestBody UserRequest req) {
        return new ResponseEntity<>(userManagementService.GetEmployeeListInfo(req, true), HttpStatus.OK);
    }

    @RequestMapping(value = "/getLoginUser", method = RequestMethod.POST)
    public ResponseEntity<?> GetLoginEmployee(@RequestBody UserRequest req) {
        return new ResponseEntity<>(userManagementService.GetLoginEmployee(req), HttpStatus.OK);
    }

    @RequestMapping(value = "/getUsersInformationByIdAndParent", method = RequestMethod.POST)
    public ResponseEntity<?> getUsersInformationByIdAndParent(@RequestBody HashMap<String, Long> targetUser) {
        return new ResponseEntity<>(userManagementService.getUsersInformationListInfo(targetUser), HttpStatus.OK);
    }


    @RequestMapping(value = "/portalAddTenantUser", method = RequestMethod.POST)
    public ResponseEntity<?> portalAddTenantUser(@RequestBody TenantGroupsUserRequest req) throws Exception {
        return new ResponseEntity<>(userManagementService.addTenantUser(req, Boolean.TRUE), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllGroupsDetails", method = RequestMethod.POST)
    public ResponseEntity<?> getAllGroupsDetails(@RequestBody List<Long> groupsIds) {
        return new ResponseEntity<>(exportImportService.getAllGroupsDetails(groupsIds), HttpStatus.OK);
    }

    @RequestMapping(value = "/importGroupDetails", method = RequestMethod.POST)
    public ResponseEntity<?> importGroupDetails(@RequestBody ImportGroupRequest req) {
        return new ResponseEntity<>(exportImportService.importGroupDetails(req, true), HttpStatus.OK);
    }

    @RequestMapping(value = "/getEmployeesDetails", method = RequestMethod.POST)
    public ResponseEntity<?> getAllEmployeesDetails(@RequestBody List<Long> employeesIds) {
        return new ResponseEntity<>(exportImportService.getAllEmployeesDetails(employeesIds), HttpStatus.OK);
    }

    @RequestMapping(value = "/importEmployeeDetails", method = RequestMethod.POST)
    public ResponseEntity<?> importEmployeeDetails(@RequestBody ImportEmployeeRequest req) {
        return new ResponseEntity<>(exportImportService.importEmployeeDetails(req), HttpStatus.OK);
    }

    @RequestMapping(value = "/updateEmployeeInfoFromPortal", method = RequestMethod.POST)
    public ResponseEntity<?> updateEmployeeInfoFromPortal(@RequestBody HashMap<String,String> customerObj) throws Exception {
        return new ResponseEntity<>(userManagementService.updateEmployeeInfoFromPortal(customerObj), HttpStatus.OK);
    }

}
