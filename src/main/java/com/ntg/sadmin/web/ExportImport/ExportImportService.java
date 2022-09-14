package com.ntg.sadmin.web.ExportImport;

import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.entities.Group;
import com.ntg.sadmin.data.repositories.GroupRepository;
import com.ntg.sadmin.data.service.EmployeeEntityService;
import com.ntg.sadmin.data.service.GroupEntityService;
import com.ntg.sadmin.utils.Utils;
import com.ntg.sadmin.web.dto.SessionInfo;
import com.ntg.sadmin.web.requests.AddEmployeeRequest;
import com.ntg.sadmin.web.requests.GroupMembersRequest;
import com.ntg.sadmin.web.requests.ImportEmployeeRequest;
import com.ntg.sadmin.web.requests.ImportGroupRequest;
import com.ntg.sadmin.web.response.*;
import com.ntg.sadmin.web.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahmoud Atef
 */
@Service
public class ExportImportService {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private EmployeeEntityService employeeRepo;

    @Autowired
    private GroupEntityService groupRepo;

    @Autowired
    private GroupRepository groupRepository;

    public GroupsResponse getAllGroupsDetails(List<Long> groupsIds) {
        GroupsResponse response = new GroupsResponse();
        List<Group> groups = groupRepo.findAllGroupsByIds(groupsIds);
        response.groups = mapGroupsResponse(groups);
        return response;
    }

    private List<GroupAllDetails> mapGroupsResponse(List<Group> groups) {
        List<GroupAllDetails> groupsDetails = new ArrayList<>();
        if (Utils.isNotEmpty(groups)) {
            for (Group group : groups) {
                GroupAllDetails details = mapGroupsDetailsObject(group);
                groupsDetails.add(details);
            }
        }
        return groupsDetails;
    }

    private GroupAllDetails mapGroupsDetailsObject(Group group) {
        GroupAllDetails details = new GroupAllDetails();
        details.setDefault(group.isDefault);
        details.setDescription(group.Description);
        details.setGroupId(group.Group_ID);
        details.setEmail(group.Email);
        details.setGroupType(group.gruop_type);
        details.setGroupPrimeId(group.GroupPrimeID);
        details.setName(group.Name);
        details.setHaveAdminPrev(group.isHaveAdminPrev);
        details.setStatusId(group.Status_ID);
        details.setStatusName(group.statusName);
        if (Utils.isNotEmpty(group.parentGroup)) {
            details.setParentGroup(mapGroupsDetailsObject(group.parentGroup));
        }
        return details;
    }

    public EmployeesDetailsResponse getAllEmployeesDetails(List<Long> employeesIds) {
        EmployeesDetailsResponse response = new EmployeesDetailsResponse();
        List<Employee> employees = employeeRepo.findEmployeesByIds(employeesIds);
        response.employees = mapEmployeesDetailsResponse(employees);
        return response;
    }

    private List<EmployeeDetails> mapEmployeesDetailsResponse(List<Employee> employees) {
        List<EmployeeDetails> employeesDetails = new ArrayList<>();
        if (Utils.isNotEmpty(employees)) {
            for (Employee employee : employees) {
                EmployeeDetails details = mapEmployeeDetailsObject(employee);
                employeesDetails.add(details);
            }
        }
        return employeesDetails;
    }

    private EmployeeDetails mapEmployeeDetailsObject(Employee employee) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        if (Utils.isNotEmpty(employee)) {
            employeeDetails = new EmployeeDetails();
            employeeDetails.setName(employee.getName());
            employeeDetails.setFirstName(employee.getName_First());
            employeeDetails.setLastName(employee.getName_Last());
            employeeDetails.setEmail(employee.getEmail());
            employeeDetails.setUserName(employee.getUser_Name());
            employeeDetails.setUserType(employee.getUserType());
            employeeDetails.setLoginType(employee.getLoginType());
            employeeDetails.setPassword(employee.getPassword());
            employeeDetails.setPrefereLanguage(employee.getPrefered_Language());
            employeeDetails.setMobilePhone(employee.getMobile_Phone());
            employeeDetails.setAccountTemporaryLockedStartTime(employee.getAccountTemporaryLockedStartTime());
            if (employee.PARENT_ID != null && employee.PARENT_ID > 0) {
                Employee parentEmployee = employeeRepo.findEmployeeById(employee.PARENT_ID);
                if (Utils.isNotEmpty(parentEmployee)) {
                    employeeDetails.setParentEmployee(mapEmployeeDetailsObject(parentEmployee));
                }
            }
            employeeDetails.setCorporateId(employee.getCORPORATE_ID());
            employeeDetails.setContractTypeId(employee.getContractTypeID());
            employeeDetails.setDeptId(employee.getDeptID());
            employeeDetails.setExpireDate(employee.getExpire_Date());
            employeeDetails.setFailedLoginCounter(employee.getFailedLoginCounter());
            employeeDetails.setGender(employee.getGender());
            employeeDetails.setHourCost(employee.getHourCost());
            if (Utils.isNotEmpty(employee.getImage())) {
                employeeDetails.setImage(new String(employee.getImage(), StandardCharsets.UTF_8));
            }
            if (Utils.isNotEmpty(employee.getSmallImage())) {
                employeeDetails.setSmallImage(new String(employee.getSmallImage(), StandardCharsets.UTF_8));
            }
            employeeDetails.setLocationName(employee.getLocationName());
            employeeDetails.setSkills(employee.getSkills());
            employeeDetails.setSms2faCode(employee.getSms_2fa_code());
            employeeDetails.setSms2faEnabled(employee.getSms_2fa_enabled());
            employeeDetails.setSms_expire_code_Number(employee.getSms_expire_code_Number());
            employeeDetails.setSmsExpireTime(employee.getSms_expire_time());
            employeeDetails.setSuperAdmin(employee.getIsSuperAdmin());
            employeeDetails.setStartWorkingHour(employee.getStartWorkingHour());
            employeeDetails.setWorkingDays(employee.getWorkingDays());
            employeeDetails.setWorkingHours(employee.getWorkinghours());
            employeeDetails.setStatusId(employee.getStatus_ID());
            employeeDetails.setStatusName(employee.getStatusName());
            employeeDetails.setTemporaryLockCounter(employee.getTemporaryLockCounter());
            if (Utils.isNotEmpty(employee.getUserGroups())) {
                List<GroupAllDetails> groups = new ArrayList<>();
                for (Group group : employee.getUserGroups()) {
                    GroupAllDetails groupIE = mapGroupsDetailsObject(group);
                    groups.add(groupIE);
                }
                employeeDetails.setUserGroups(groups);
            }
        }
        return employeeDetails;
    }

    public Long importGroupDetails(ImportGroupRequest req, boolean makeUserGroupRelation) {
        TenantContext.setCompanyName(req.getCompanyName());
        SessionInfo loginUserInfo = req.getLoginUserInfo();
        GroupAllDetails group = req.getGroup();
        Long groupId = null;
        if (Utils.isNotEmpty(group)) {
            if (group.getGroupType() == 1) {
                groupId = importCompanyDetails(group, loginUserInfo);
            } else if (group.getGroupType() == 2) {
                groupId = importOrganizationDetails(group, loginUserInfo, null);
            } else {
                groupId = importGroupObjectDetails(group, loginUserInfo, makeUserGroupRelation);
            }
        }
        return groupId;
    }

    private Long importGroupObjectDetails(GroupAllDetails groupObj, SessionInfo loginUserInfo, boolean makeUserGroupRelation) {
        Long groupId = null;
        if (Utils.isNotEmpty(groupObj) && Utils.isNotEmpty(groupObj.getParentGroup()) && Utils.isNotEmpty(groupObj.getParentGroup().getParentGroup())) {
            Long companyId = importCompanyDetails(groupObj.getParentGroup().getParentGroup(), loginUserInfo);
            Long organizationId = importOrganizationDetails(groupObj.getParentGroup(), loginUserInfo, companyId);
            if (organizationId != null && organizationId > 0) {
                Group group = groupRepo.findByNameAndParentId(groupObj.getName(), organizationId);
                if (Utils.isNotEmpty(group)) {
                    groupId = group.Group_ID;
                } else {
                    Group groupDetails = mapGroupsDetailsObjectToSave(groupObj);
                    groupDetails.PARENT_GROUP_ID = organizationId;
                    groupDetails.CREATED_BY_NAME = loginUserInfo.loginUserName;
                    groupDetails.CREATED_BY_COMPANY = loginUserInfo.companyName;
                    group = groupRepo.save(groupDetails);
                    groupId = group.Group_ID;
                    // add current user to the created group
                    if (makeUserGroupRelation && groupId != null && groupId > 0) {
                        GroupMembersRequest grpMemReq = new GroupMembersRequest();
                        grpMemReq.LoginUserInfo = loginUserInfo;
                        Long userId = null;
                        if (Utils.isNotEmpty(loginUserInfo.userID)) {
                            userId = Long.parseLong(loginUserInfo.userID);
                        } else {
                            Employee emp = employeeRepo.findByUsername(loginUserInfo.loginUserName);
                            if (Utils.isNotEmpty(emp)) {
                                userId = emp.Emp_ID;
                            }
                        }
                        grpMemReq.UserId = userId;
                        grpMemReq.GroupId = groupId;
                        userManagementService.AddUserToGroup(grpMemReq);
                    }
                }
            }
        }
        return groupId;
    }

    private Long importOrganizationDetails(GroupAllDetails organizationObj, SessionInfo loginUserInfo, Long companyGroupId) {
        Long companyId = (companyGroupId != null) ? companyGroupId : importCompanyDetails(organizationObj.getParentGroup(), loginUserInfo);
        Long organizationId = null;
        if (companyId != null && companyId > 0) {
            Group organization = groupRepo.findByNameAndParentId(organizationObj.getName(), companyId);
            if (Utils.isNotEmpty(organization)) {
                organizationId = organization.Group_ID;
            } else {
                Group organizationDetails = mapGroupsDetailsObjectToSave(organizationObj);
                organizationDetails.PARENT_GROUP_ID = companyId;
                organizationDetails.CREATED_BY_NAME = loginUserInfo.loginUserName;
                organizationDetails.CREATED_BY_COMPANY = loginUserInfo.companyName;
                organization = groupRepo.save(organizationDetails);
                organizationId = organization.Group_ID;
            }
        }
        return organizationId;
    }

    private Long importCompanyDetails(GroupAllDetails companyObj, SessionInfo loginUserInfo) {
        Long companyId = null;
        Group company = groupRepo.findByNameAndParentId(companyObj.getName(), null);
        if (Utils.isNotEmpty(company)) {
            companyId = company.Group_ID;
        } else {
            Group companyDetails = mapGroupsDetailsObjectToSave(companyObj);
            companyDetails.CREATED_BY_NAME = loginUserInfo.loginUserName;
            companyDetails.CREATED_BY_COMPANY = loginUserInfo.companyName;
            company = groupRepo.save(companyDetails);
            companyId = company.Group_ID;
        }
        return companyId;
    }

    private Group mapGroupsDetailsObjectToSave(GroupAllDetails groupObj) {
        Group group = new Group();
        group.Name = groupObj.getName();
        group.Email = groupObj.getEmail();
        group.Description = groupObj.getDescription();
        group.Status_ID = groupObj.getStatusId();
        group.statusName = groupObj.getStatusName();
        group.GroupPrimeID = groupObj.getGroupPrimeId();
        group.gruop_type = groupObj.getGroupType();
        group.isDefault = groupObj.getDefault();
        group.isHaveAdminPrev = groupObj.getHaveAdminPrev();
        return group;
    }

    public Long importEmployeeDetails(ImportEmployeeRequest req) {
        Long employeeId = null;
        TenantContext.setCompanyName(req.getCompanyName());
        SessionInfo loginUserInfo = req.getLoginUserInfo();
        EmployeeDetails employeeIE = req.getEmployee();
        employeeIE.setFailedLoginCounter(req.getNumberOfUsers());
        if (Utils.isNotEmpty(employeeIE)) {
            Employee employee = mapEmployeeDetailsObjectToSave(employeeIE);

            // import employee groups
            List<Long> userGroups = new ArrayList<>();
            if (Utils.isNotEmpty(employeeIE.getUserGroups())) {
                for (GroupAllDetails groupIE : employeeIE.getUserGroups()) {
                    if (groupIE.getGroupType() == 3) {
                        ImportGroupRequest groupReq = new ImportGroupRequest();
                        groupReq.setGroup(groupIE);
                        groupReq.setCompanyName(req.getCompanyName());
                        groupReq.setLoginUserInfo(loginUserInfo);
                        Long groupId = importGroupDetails(groupReq, false);
                        if (Utils.isNotEmpty(groupId)) {
                            userGroups.add(groupId);
                        }
                    }
                }
            }
            // import employee manager
            if (Utils.isNotEmpty(employeeIE.getParentEmployee())) {
                ImportEmployeeRequest parentReq = new ImportEmployeeRequest();
                parentReq.setEmployee(employeeIE.getParentEmployee());
                parentReq.setCompanyName(req.getCompanyName());
                parentReq.setLoginUserInfo(loginUserInfo);
                parentReq.setNumberOfUsers(req.getNumberOfUsers());
                Long managerId = importEmployeeDetails(parentReq);
                if (Utils.isNotEmpty(managerId)) {
                    employee.setPARENT_ID(managerId);
                }
            }
            AddEmployeeRequest request = new AddEmployeeRequest();
            request.companyName = req.getCompanyName();
            request.emp = employee;
            StringResponse response = userManagementService.CRMAddEmployee(request, true);
            // add user groups relation
            if (Utils.isNotEmpty(response) && Utils.isNotEmpty(response.returnValue)
                    && Utils.isEmpty(response.getRestException())) {
                employeeId = Long.parseLong(response.returnValue);
                if (Utils.isNotEmpty(userGroups)) {
                    for (Long groupId : userGroups) {
                        Long count = groupRepository.checkIfUserExistsInGroup(groupId, employeeId);
                        if (count == 0) {
                            GroupMembersRequest grpMemReq = new GroupMembersRequest();
                            grpMemReq.UserId = employeeId;
                            grpMemReq.GroupId = groupId;
                            userManagementService.AddUserToGroup(grpMemReq);
                        }
                    }
                }
            }
        }
        return employeeId;
    }

    private Employee mapEmployeeDetailsObjectToSave(EmployeeDetails employeeDetails) {
        Employee employee = new Employee();
        employee.setName(employeeDetails.getName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPassword(employeeDetails.getPassword());
        employee.setSkills(employeeDetails.getSkills());
        employee.setUser_Name(employeeDetails.getUserName());
        employee.setName_First(employeeDetails.getFirstName());
        employee.setName_Last(employeeDetails.getLastName());
        employee.setMobile_Phone(employeeDetails.getMobilePhone());
        employee.setStatus_ID(employeeDetails.getStatusId());
        employee.setLoginType(employeeDetails.getLoginType());
        employee.setCORPORATE_ID(employeeDetails.getCorporateId());
        employee.setDeptID(employeeDetails.getDeptId());
        employee.setLocationName(employeeDetails.getLocationName());
        employee.setStatusName(employeeDetails.getStatusName());
        employee.setHourCost(employeeDetails.getHourCost());
        employee.setWorkinghours(employeeDetails.getWorkingHours());
        employee.setWorkingDays(employeeDetails.getWorkingDays());
        employee.setStartWorkingHour(employeeDetails.getStartWorkingHour());
        employee.setExpire_Date(employeeDetails.getExpireDate());
        employee.setPrefered_Language(employeeDetails.getPrefereLanguage());
        employee.setGender(employeeDetails.getGender());
        employee.setContractTypeID(employeeDetails.getContractTypeId());
        if (Utils.isNotEmpty(employeeDetails.getImage())) {
            employee.setImage(employeeDetails.getImage().getBytes(StandardCharsets.UTF_8));
        }
        if (Utils.isNotEmpty(employeeDetails.getSmallImage())) {
            employee.setSmallImage(employeeDetails.getSmallImage().getBytes(StandardCharsets.UTF_8));
        }
        employee.setUserType(employeeDetails.getUserType());
        employee.setAccountTemporaryLockedStartTime(employeeDetails.getAccountTemporaryLockedStartTime());
        employee.setTemporaryLockCounter(employeeDetails.getTemporaryLockCounter());
        employee.setFailedLoginCounter(employeeDetails.getFailedLoginCounter());
        employee.setIsSuperAdmin(employeeDetails.getSuperAdmin());
        employee.setSms_2fa_enabled(employeeDetails.getSms2faEnabled());
        employee.setSms_2fa_code(employeeDetails.getSms2faCode());
        employee.setSms_expire_code_Number(employeeDetails.getSms_expire_code_Number());
        employee.setSms_expire_time(employeeDetails.getSmsExpireTime());
        return employee;
    }
}
