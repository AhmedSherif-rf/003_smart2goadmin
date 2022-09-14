package com.ntg.sadmin.web.services;

import com.ntg.common.STAGESystemOut;
import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.common.dbcompatibilityhelper.SqlHelper;
import com.ntg.sadmin.constants.CodesAndKeys;
import com.ntg.sadmin.constants.CommonConstants;
import com.ntg.sadmin.constants.ConfigurationConstant;
import com.ntg.sadmin.data.entities.Configuration;
import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.entities.Group;
import com.ntg.sadmin.data.repositories.ConfigurationRepository;
import com.ntg.sadmin.data.repositories.GroupRepository;
import com.ntg.sadmin.data.service.ConfigurationEntityService;
import com.ntg.sadmin.data.service.EmployeeEntityService;
import com.ntg.sadmin.data.service.GroupEntityService;
import com.ntg.sadmin.exceptions.BusinessException;
import com.ntg.sadmin.exceptions.NTGRestException;
import com.ntg.sadmin.utils.EncryptionUtils;
import com.ntg.sadmin.utils.ObjectMapperUtils;
import com.ntg.sadmin.utils.Utils;
import com.ntg.sadmin.web.dto.*;
import com.ntg.sadmin.web.requests.*;
import com.ntg.sadmin.web.response.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author abdelrahman
 */
@Service
public class UserManagementService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeeEntityService employeeRepo;

    @Autowired
    private GroupEntityService groupRepo;

    @Autowired
    private SqlHelper sqlHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfigurationEntityService configurationEntityService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;


    /**
     * @param req
     * @return
     */
    // encryptPassword flag is added to prevent password encryption in case of create Instance User request
    // that coming from portal as password is already encrypted in portal
    public StringResponse CRMAddEmployee(AddEmployeeRequest req, Boolean encryptPassword) {

        // set company from request
        TenantContext.setCompanyName(req.companyName);
        // Check license limitaions
        long LicenseConfiguration_numberOfUsers = req.emp.getFailedLoginCounter();
        long CurrentNoUsers = employeeRepo.GetEmployeeCountsInDB();
        req.emp.setFailedLoginCounter(0l);
        if (CurrentNoUsers >= LicenseConfiguration_numberOfUsers) {
            throw new BusinessException("You have exceeded the allowed number of users");
        }


        Employee emp = employeeRepo.findByUsername(req.emp.User_Name);
        if (emp != null) {
            StringResponse res = new StringResponse("User_Name Already exists !");
            res.setRestException(new NTGRestException("SAdnub001", "User_Name Already exists !"));
            return res;
        }
        if (Utils.isNotEmpty(req.emp.Email)) {
            Employee empMail = employeeRepo.findByMail(req.emp.Email);

            if (empMail != null) {
                StringResponse res = new StringResponse("Email Already exists !");
                res.setRestException(new NTGRestException("SAdnub001", "Email Already exists !"));
                return res;
            }
        }

        // Check Password Strength Rules , then Encrypt Password before saving user details
//        if (!Utils.validatePasswordStrength(req.emp.Password)) {
//            throw new BusinessException(CodesAndKeys.INVALID_PASSWORD_STRENGTH_CODE, CodesAndKeys.INVALID_PASSWORD_STRENGTH_KEY, CodesAndKeys.INVALID_PASSWORD_STRENGTH_MESSAGE);
//        }
        if (encryptPassword) {
            req.emp.Password = passwordEncoder.encode(req.emp.Password);
        }

        emp = req.emp;
        //fix return id not be employe reall id not zero as for wired reason the save of the rep not set the id

        emp = employeeRepo.save(emp);

        return new StringResponse(emp.Emp_ID.toString());


    }

    // to delete user

    /**
     * @param req
     * @return
     */
    public StringResponse DeleteUser(DeleteUserRequest req) {

        String deleteFromGroup = "delete from sa_user_group where userid = " + req.RecID;
        String deleteUser = "delete from comp_employee where recid = " + req.RecID;
        jdbcTemplate.execute(deleteFromGroup);
        jdbcTemplate.execute(deleteUser);

        return new StringResponse("Employee with id :" + req.RecID + ":deleted ");


    }

    // to update user

    /**
     * @param req
     * @return
     */
    public StringResponse updateEmployee(AddEmployeeRequest req) throws NTGRestException {

        // set company from request
        TenantContext.setCompanyName(req.companyName);

        Employee emp = employeeRepo.findOne(req.emp.Emp_ID);
        emp.setOtp(req.emp.otp);
        emp.setOtpValidUntil(req.emp.otpValidUntil);
        Long loginType = null;
        //only admin can update status
        if(req.canUpdateStatus==null || !req.canUpdateStatus){
            req.emp.setStatusName(emp.getStatusName());
            req.emp.setStatus_ID(emp.getStatus_ID());
        }
        if (emp == null) {
            throw new NTGRestException("", "This Employee Doesn't Exist");
        } else {

            // check if updated name or email already exists
            Employee empName = employeeRepo.findByUsernameAndId(req.emp.User_Name, req.emp.Emp_ID);
            if (empName != null) {
                StringResponse res = new StringResponse("User_Name Already exists !");
                res.setRestException(new NTGRestException("SAdnub001", "User_Name Already exists !"));
                return res;
            }

            if (Utils.isNotEmpty(req.emp.Email)) {
                Employee empMail = employeeRepo.findByMailANDId(req.emp.Email, req.emp.Emp_ID);

                if (empMail != null) {
                    StringResponse res = new StringResponse("Email Already exists !");
                    res.setRestException(new NTGRestException("SAdnub001", "Email Already exists !"));
                    return res;
                }
            }

            // to handle in case of login type updated from request
            if (Utils.isNotEmpty(req.emp.loginType) && Utils.isNotEmpty(emp.getLoginType()) && req.emp.loginType.longValue() != emp.getLoginType().longValue()) {
                loginType = req.emp.loginType;
            } else if (Utils.isNotEmpty(emp.getLoginType())) {
                loginType = req.emp.loginType;
            }

            if (loginType != null && loginType.intValue() == CommonConstants.LOGIN_TYPE_DB) {
                // if Request Password equals DB Password, Do Not Update
                if ((Utils.isNotEmpty(emp.Password) && Utils.isNotEmpty(req.emp.Password)
                        && !emp.Password.equals(req.emp.Password)) || (Utils.isEmpty(emp.Password) && Utils.isNotEmpty(req.emp.Password))) {

                    if (!Utils.validatePasswordStrength(req.emp.Password)) {
                        throw new BusinessException(CodesAndKeys.INVALID_PASSWORD_STRENGTH_CODE,
                                CodesAndKeys.INVALID_PASSWORD_STRENGTH_KEY,
                                CodesAndKeys.INVALID_PASSWORD_STRENGTH_MESSAGE);
                    }
                    // Encrypt Request Password
                    if (Utils.isNotEmpty(req.emp.Password))
                        req.emp.Password = passwordEncoder.encode(req.emp.Password);
                }
            } else {
                // this to execlude password from update
                req.emp.Password = emp.Password;
            }
            boolean isSuperAdmin = emp.getIsSuperAdmin();
            if(req.emp.getPassword() != null && !req.emp.getPassword().isEmpty()
                    && req.emp.getEmp_ID() !=null && req.emp.getEmp_ID() > 0)
                employeeRepo.updatePassword(req.emp.getPassword() , req.emp.getEmp_ID() , req.companyName);
            emp = req.emp;
            emp.setIsSuperAdmin(isSuperAdmin);
            employeeRepo.save(emp);
            return new StringResponse("Employee With ID: " + emp.Emp_ID.toString() + ", Updated Successfully");
        }
    }

    // to create group
    // @Transactional

    /**
     * @param req
     * @return
     * @throws Exception
     */
//@addedBy:Aya.Ramadan=>Dev-00002237 :Not able to add organization/group with the same name of organization/group in different company"
    public AddGroupResponse createGroup(com.ntg.sadmin.web.requests.AddGroupRequest req) throws Exception {
        STAGESystemOut.PrintNonDebugError("Enter createGroup --> 1 ");
        // set company from request
        TenantContext.setCompanyName(req.companyName);

        SessionInfo LoginUserInfo = req.LoginUserInfo;

        AddGroupResponse res = new AddGroupResponse();

        STAGESystemOut.PrintNonDebugError("Enter createGroup --> 2 [findByName] ");
        Group group = groupRepo.findByNameAndParentId(req.group.Name, req.group.PARENT_GROUP_ID);
        if (group != null) {
            STAGESystemOut.PrintNonDebugError("Enter createGroup --> 3 [findByName] --> found  " + group.Name);
            //throw new NTGRestException("", "groupName already Exists !");
            if(group.gruop_type==1){throw new Exception("Company Name already Exists : " + group.Name); }
            else if(group.gruop_type == 2){throw new Exception("Organization Name already Exists : " + group.Name); }
            else{throw new Exception("Group Name already Exists : " + group.Name);}
        } else {
            STAGESystemOut.PrintNonDebugError("Enter createGroup --> 4 [findByName] --> Not FOund ");
            group = req.group;
            group.CREATED_BY_NAME = LoginUserInfo.loginUserName;
            group.CREATED_BY_COMPANY = LoginUserInfo.companyName;
            // group.CREATION_DATE = (Timestamp) new Date();
            STAGESystemOut.PrintNonDebugError("Enter createGroup --> 5 [save]  ");
            group = groupRepo.save(req.group);
            STAGESystemOut.PrintNonDebugError("Enter createGroup --> 6 [saved]  " + group.Group_ID.toString());
            res.setReturnValue(group.Group_ID.toString());
            return res;
        }


    }

    // to Edit group
    // @Transactional

    /**
     * @param req
     * @return
     * @throws Exception
     */
    public AddGroupResponse EditGroup(com.ntg.sadmin.web.requests.AddGroupRequest req) throws Exception {

        // set company from request
        TenantContext.setCompanyName(req.companyName);

        AddGroupResponse res = new AddGroupResponse();
        Group group = groupRepo.findByName(req.group.Name);
        if (group != null && !req.group.Group_ID.equals(group.Group_ID)) {
            if(group.gruop_type==1){throw new Exception("Company Name already Exists : " + group.Name); }
            else if(group.gruop_type == 2){throw new Exception("Organization Name already Exists : " + group.Name); }
            else{throw new Exception("Group Name already Exists : " + group.Name);}
        }

        Group groupById = groupRepo.findOne(req.group.Group_ID);
        if (groupById == null) {
            throw new NTGRestException("", "This group doesn't Exist!");
        } else {
            int haveAdminPrev = 0;
            if (req.group.isHaveAdminPrev != null) {
                if (req.group.isHaveAdminPrev) {
                    haveAdminPrev = 1;
                }
            }
            String updateGroup = "update sa_group set NAME = '" + req.group.Name + "',EMAIL = '" + req.group.Email
                    + "', DESCRIPTION ='" + req.group.Description + "', STATUSID =" + req.group.Status_ID
                    + ",GRUOP_TYPE =" + req.group.gruop_type + ",PARENT_GROUP_ID =" + req.group.PARENT_GROUP_ID
                    + ",is_have_admin_prev ='" + haveAdminPrev
                    + "', STATUS_NAME ='" + req.group.statusName +  "', cc_list ='" + req.group.ccList + "'"
                    + " where RECID = " + req.group.Group_ID;
            jdbcTemplate.update(updateGroup);
            res.setReturnValue("Updated Successfully!");
            return res;
        }

    }

    // to get all members in group

    /**
     * @param req
     * @return
     */
    @Transactional
    public GetGroupMembersResponse GetGroupMember(GetGroupMembersRequest req) {

        ArrayList<com.ntg.sadmin.web.dto.Employee> returnValue = new ArrayList<com.ntg.sadmin.web.dto.Employee>();
        Group group = null;

        group = groupRepo.findOne(req.RecID);
        if (group == null) {
            throw new NTGRestException("", "this group doesnt Exist!");
        }
        if (group.groupUsers.size() <= 0) {
            return null;
        } else {

            for (Employee emp : group.groupUsers) {
                com.ntg.sadmin.web.dto.Employee mappedEmp = new com.ntg.sadmin.web.dto.Employee();
                mappedEmp.GroupID = req.RecID;
                mappedEmp.IsMember = true;
                mappedEmp.EmployeeName = emp.Name;
                mappedEmp.email = emp.Email;
                mappedEmp.name = emp.Name;

                mappedEmp.UserID = emp.Emp_ID;
                mappedEmp.NAME_FIRST = emp.Name_First;
                mappedEmp.NAME_LAST = emp.Name_Last;
                mappedEmp.MemberEmail = emp.Email;
                mappedEmp.MemberPhoneNumber = emp.Mobile_Phone;
                mappedEmp.MemberUserName = emp.User_Name;
                mappedEmp.MemberUserName = emp.User_Name;
                mappedEmp.MemberUserName = emp.User_Name;

                // check manger data of employee
                if (emp.PARENT_ID != null && emp.PARENT_ID > 0) {
                    Employee manager = employeeRepo.findOne(emp.PARENT_ID);
                    mappedEmp.ManagerName = manager.Name;
                    mappedEmp.MANAGER_EMAIL = manager.Email;
                    mappedEmp.ManagerPhoneNumber = manager.Mobile_Phone;
                    mappedEmp.ManagerUserName = manager.User_Name;
                }
                returnValue.add(mappedEmp);

            }
            return new GetGroupMembersResponse((com.ntg.sadmin.web.dto.Employee[]) returnValue
                    .toArray(new com.ntg.sadmin.web.dto.Employee[returnValue.size()]));
        }

    }

    // Added by Asmaa to get list of employees in list of groups
    public GetGroupMembersResponse GetGroupsMembers(UserGroup[] req) {

        ArrayList<com.ntg.sadmin.web.dto.Employee> returnValue = new ArrayList<com.ntg.sadmin.web.dto.Employee>();
        Group group = null;

        for (int i = 0; i < req.length; i++) {

            group = groupRepo.findOne(req[i].GroupID);

            for (Employee emp : group.groupUsers) {
                com.ntg.sadmin.web.dto.Employee mappedEmp = new com.ntg.sadmin.web.dto.Employee();
                mappedEmp.GroupID = req[i].GroupID;
                mappedEmp.IsMember = true;
                mappedEmp.EmployeeName = emp.Name;
                mappedEmp.email = emp.Email;
                mappedEmp.name = emp.Name;

                mappedEmp.UserID = emp.Emp_ID;
                mappedEmp.NAME_FIRST = emp.Name_First;
                mappedEmp.NAME_LAST = emp.Name_Last;
                mappedEmp.MemberEmail = emp.Email;
                mappedEmp.MemberPhoneNumber = emp.Mobile_Phone;
                mappedEmp.MemberUserName = emp.User_Name;
                mappedEmp.MemberUserName = emp.User_Name;
                mappedEmp.MemberUserName = emp.User_Name;

                // check manger data of employee
                if (emp.PARENT_ID != null && emp.PARENT_ID > 0) {
                    Employee manager = employeeRepo.findOne(emp.PARENT_ID);
                    mappedEmp.ManagerName = manager.Name;
                    mappedEmp.MANAGER_EMAIL = manager.Email;
                    mappedEmp.ManagerPhoneNumber = manager.Mobile_Phone;
                    mappedEmp.ManagerUserName = manager.User_Name;
                }
                returnValue.add(mappedEmp);
            }
        }
        return new GetGroupMembersResponse((com.ntg.sadmin.web.dto.Employee[]) returnValue
                .toArray(new com.ntg.sadmin.web.dto.Employee[returnValue.size()]));


    }

    // to get employee full information

    /**
     * @param req
     * @return
     */
    public EmployeeFullInfoResponse GetEmployeeFullInformation(EmployeeFullInfoRequest req) {

        EmployeeFullInfoResponse response = new EmployeeFullInfoResponse();
        Employee emp = employeeRepo.findOne(req.empId);
        if (emp == null) {
            throw new NTGRestException("", "This Employee doesn't Exist");
        } else {
            EmployeeInfo empInfo = new EmployeeInfo();
            empInfo.ContractTypeID = emp.ContractTypeID;
            empInfo.CORPORATE_ID = emp.CORPORATE_ID;
            empInfo.DeptID = emp.DeptID;
            empInfo.Email = emp.Email;
            empInfo.Emp_ID = emp.Emp_ID;
            empInfo.Expire_Date = emp.Expire_Date;
            empInfo.HourCost = emp.HourCost;
            empInfo.image = emp.image;
            empInfo.locationName = emp.locationName;
            empInfo.loginType = emp.loginType;
            empInfo.Mobile_Phone = emp.Mobile_Phone;
            empInfo.Name = emp.Name;
            empInfo.Name_First = emp.Name_First;
            empInfo.Name_Last = emp.Name_Last;
            empInfo.PARENT_ID = emp.PARENT_ID;
            empInfo.Prefered_Language = emp.Prefered_Language;
            empInfo.SmallImage = emp.SmallImage;
            empInfo.startWorkingHour = emp.startWorkingHour;
            empInfo.Status_ID = emp.Status_ID;
            empInfo.statusName = emp.statusName;
            empInfo.User_Name = emp.User_Name;
            empInfo.WorkingDays = emp.WorkingDays;
            empInfo.workinghours = emp.workinghours;
            empInfo.gender = emp.gender;
            // add by amr to handel the employee skills
            empInfo.Skills = emp.Skills;
            empInfo.svgFlag = emp.svgFlag;
            response.returnValue = empInfo;
            return response;
        }

    }

    // to get group list

    /**
     * @return
     */
    public GetGroupsResponse getGroupList() {
        // isHaveAdminPrev

        com.ntg.sadmin.web.dto.Group group = new com.ntg.sadmin.web.dto.Group();
        ArrayList<com.ntg.sadmin.web.dto.Group> groupsRes = new ArrayList<com.ntg.sadmin.web.dto.Group>();
        Iterable<Group> groups = groupRepo.findAll();
        if (groups == null) {
            throw new NTGRestException("", "There is no groups yet , !");
        } else {
            for (Group gr : groups) {
                group.Email = gr.Email;
                group.Group_ID = gr.Group_ID;
                group.Name = gr.Name;
                group.PARENT_GROUP_ID = gr.PARENT_GROUP_ID;
                group.type = gr.gruop_type;
                groupsRes.add(group);
            }
            return new GetGroupsResponse((com.ntg.sadmin.web.dto.Group[]) groupsRes.toArray());
        }


    }

    // to get all Employees list

    /**
     * @param req
     * @return
     */
    public UserResponse GetEmployeeListInfo(UserRequest req, boolean filter) {

        UserResponse response = new UserResponse();

        List<Employee> newEmpList = new ArrayList<Employee>();
        ;

        if (filter) {
            Employee loginUser = employeeRepo.findOne(Long.parseLong(req.LoginUserInfo.userID));
            newEmpList.add(loginUser);
            GetEmployeeListOfLoginUSer(newEmpList, Long.parseLong(req.LoginUserInfo.userID));

            Employee parent = employeeRepo.findOne(loginUser.PARENT_ID);
            if (parent != null) {
                newEmpList.add(parent);
            }
        } else {
            // In new Tenants: check if login user is superAdmin if true return it at allEmployees list
            //Employee loginUser = employeeRepo.findOne(Long.parseLong(req.LoginUserInfo.userID));
            // String queryString = "SELECT * FROM comp_employee WHERE is_super_admin = '1' AND RECID = " + Long.parseLong(req.LoginUserInfo.userID);
            //List<Map<String, Object>> loginUser = jdbcTemplate.queryForList(queryString);

//            newEmpList.add(loginUser);
            newEmpList = (List<Employee>) employeeRepo.findAll();
        }


        if (newEmpList == null) {
            throw new NTGRestException("", "There is no Employees yet , !");
        } else {

            List<EmployeeInfo> returnedd = mapEmployeeResponse(newEmpList);
            response.returnValue = (EmployeeInfo[]) returnedd.toArray(new EmployeeInfo[returnedd.size()]);

            return response;
        }


    }

    // to delete group

    /**
     * @param req
     * @return
     */
    public StringResponse deleteGroup(DeleteGroupRequest req) throws Exception {

        String companyName = TenantContext.getCompanyName();
        List<Long> recId = Collections.singletonList(Long.valueOf(req.groupID));
        Long groupType = groupRepository.getGroupTypeId(Long.parseLong(req.groupID), companyName);

        if (groupType == Long.valueOf(1)) {
            //get the list of all organizations in company
            List<Long> organizationsList = groupRepository.getAllOrganizationsOfEachCompanyByRecId(recId, companyName);
            //get the list of all groups in each organization
            List<Long> groupsList = groupRepository.getAllGroupsOfEachOrganization(organizationsList, companyName);

            List<Long> usersInGroup = groupRepository.checkIfMembersInThisGroup(groupsList);

            if (usersInGroup.isEmpty()) {
                //groupRepository.deleteFromGroup(groupsList);
                //delete groups
                groupRepository.deleteGroupsByRecId(groupsList, companyName);
                //delete organizations
                groupRepository.deleteOrganizationsByRecId(organizationsList, companyName);
            } else {
                throw new Exception("There are Employees in this company, Please delete them first");
            }

        } else if (groupType == Long.valueOf(2)) {
            //get the list of all groups in organization
            List<Long> groupsList = groupRepository.getAllGroupsOfEachOrganization(recId, companyName);

            List<Long> usersInGroup = groupRepository.checkIfMembersInThisGroup(groupsList);

            if (usersInGroup.isEmpty()) {

                //groupRepository.deleteFromGroup(groupsList);
                //delete groups
                groupRepository.deleteGroupsByRecId(groupsList, companyName);

            } else {
                throw new Exception("There are Employees in this organization, Please delete them first");
            }

        } else if (groupType == Long.valueOf(3)) {

            List<Long> usersInGroup = groupRepository.checkIfMembersInThisGroup(recId);

            if (!usersInGroup.isEmpty()) {
                throw new Exception("There are Employees in this group, Please delete them first");
            }
        }

        String deleteCompany = "delete from sa_group where recid = " + Long.parseLong(req.groupID);
        jdbcTemplate.execute(deleteCompany);

        return new StringResponse("Group with id :" + req.groupID + ":deleted ");
    }

    // to get all group where status id =1

    /**
     * @return
     */
    public GetCorporationResponse GetAllGroupsLightInfo(SessionInfo loginUserInfo) {
        GetCorporationResponse res = new GetCorporationResponse();

        ArrayList<Corporation> returnValue = new ArrayList<>();
        List<Group> groups = groupRepo.findAll(loginUserInfo.companyName);
        if (groups == null) {
            throw new NTGRestException("", "There is no groups yet , !");
        } else {
            for (Group group : groups) {
                Corporation corporation = new Corporation();
                corporation.Email = group.Email;
                corporation.Group_ID = group.Group_ID;
                corporation.gruop_type = group.gruop_type;
                corporation.Name = group.Name;
                corporation.PARENT_GROUP_ID = group.PARENT_GROUP_ID;
                corporation.Description = group.Description;
                corporation.Status_ID = group.Status_ID;
                // added for sadmin by Abdulrahman comment if any problem
                // with itts.
                corporation.isHaveAdminPrev = group.isHaveAdminPrev;
                corporation.ccList = group.ccList;
                returnValue.add(corporation);

            }
            res.returnValue = (Corporation[]) returnValue.toArray(new Corporation[returnValue.size()]);
            return res;
        }


    }

    // to add user to group

    /**
     * @param req
     * @return
     */
    public StringResponse AddUserToGroup(GroupMembersRequest req) {

        String sql = "insert into sa_user_group (recid,groupID,userid) VALUES (" + sqlHelper.SequenceFetch("sa_user_group_s") + "," + req.GroupId + "," + req.UserId + ")";
        jdbcTemplate.execute(sql);
        return new StringResponse("Employee Assigned to groupSuccessfully");

    }

    // to remove user from group

    /**
     * @param req
     * @return
     */
    public StringResponse RemoveUserFromGroup(GroupMembersRequest req) {
        String sql = "delete from sa_user_group u where u.groupID = " + req.GroupId + " and userid=" + req.UserId;
        jdbcTemplate.execute(sql);
        return new StringResponse("Employee Removed from Group");
        // groupRepo.deletUserFromGroup(req.GroupId, req.UserId);

    }

    // to get all employess by group name

    /**
     * @param req
     * @return
     */
    public GetGroupMembersResponse GetMembersByGroupName(GetGroupMembersRequest req) {
        com.ntg.sadmin.web.dto.Employee mappedEmp = new com.ntg.sadmin.web.dto.Employee();
        ArrayList<com.ntg.sadmin.web.dto.Employee> returnValue = new ArrayList<>();
        Group group = null;

        group = groupRepo.findByName(req.groupName);
        if (group == null) {
            throw new NTGRestException("", "this group doesnt Exist!");
        }
        if (group.groupUsers.size() <= 0) {
            throw new NTGRestException("", "no Users For this group");
        } else {

            for (Employee emp : group.groupUsers) {
                mappedEmp = new com.ntg.sadmin.web.dto.Employee();
                mappedEmp.EmployeeName = emp.Name;
                mappedEmp.GroupID = req.RecID;
                mappedEmp.email = emp.Email;
                mappedEmp.UserID = emp.Emp_ID;
                mappedEmp.IsMember = true;
                returnValue.add(mappedEmp);

            }

            return new GetGroupMembersResponse((com.ntg.sadmin.web.dto.Employee[]) returnValue
                    .toArray(new com.ntg.sadmin.web.dto.Employee[returnValue.size()]));

//				return new GetGroupMembersRes(null,
//						(com.ntg.crm.extrnal.dictionary.UserMgm.Employee[]) returnValue.toArray());
        }

    }

    // to get list of groups that employee participate in

    /**
     * @param req
     * @return
     */
    public EmployeeGroupListResponse getEmployeeGroupsList(EmployeeGroupListRequest req) {

        EmployeeGroupListResponse res = new EmployeeGroupListResponse();
        ArrayList<UserGroup> returnValue = new ArrayList<>();
        Employee emp = null;

        emp = employeeRepo.findOne(req.empId);
        if (emp == null) {
            //Dev-00001544 throwing exception was casing issue in adding new user to a group
            res.returnValue = null;
            return res;
        }

        if (emp.userGroups.size() <= 0) {
            //Dev-00001544 throwing exception was casing issue in adding new user to a group
            res.returnValue = null;
            return res;
        } else {

            for (Group group : emp.userGroups) {
                UserGroup mappedGroup = new UserGroup();

                mappedGroup.Group_Name = group.Name;
                mappedGroup.GroupID = group.Group_ID;
                mappedGroup.UserID = emp.Emp_ID;
                mappedGroup.isdefault = (group.isDefault == null) ? false : group.isDefault;
                mappedGroup.groupStatusName = group.statusName;

                returnValue.add(mappedGroup);

            }
            res.returnValue = (UserGroup[]) returnValue.toArray(new UserGroup[returnValue.size()]);
            return res;
        }

    }

    /**
     * @param req
     * @return
     */
    public List<GroupDetails> getGroupHierarchy(GroupDetailsRequest req) {
        ArrayList<GroupDetails> list = new ArrayList<>();
        for (Long groupId : req.groupID) {
            GroupDetails gr = new GroupDetails();
            Group group = groupRepo.findOne(groupId);
            if (group != null) {
                if (group.gruop_type == 1) {
                    gr.setGroupID(groupId);
                    gr.setCompanyName(group.Name);
                }
                if (group.gruop_type == 2) {
                    gr.setGroupID(groupId);
                    gr.setOrganizationName(group.Name);
                }
                if (group.gruop_type == 3) {
                    gr.setGroupID(groupId);
                    gr.setGroupName(group.Name);
                }
                list.add(gr);
            }
        }
        return list;
    }

    /**
     * @param req
     * @return Full group hierarchy with all details
     */
    @Transactional
    public Group getGroupHierarchyDetails(GroupDetailsRequest req) {

        Group re = groupRepo.findOne(req.groupID[0]);
        return re;
    }

    /**
     * @param emp_id
     * @return
     */
    public String wf_GetEmail(Long emp_id) {
        String re = null;
        if (emp_id != null) {
            Employee emp = employeeRepo.findOne(emp_id);
            if (emp != null && emp.Email != null) {
                re = emp.Email;
            } else {
                return null;
            }
        }
        return re;


    }

    /**
     * @param group_id
     * @return
     */
    public String wf_GetGroupEmail(Long group_id) {
        String re = null;
        if (group_id != null) {
            Group group = groupRepo.findOne(group_id);
            if (group != null && group.Email != null) {
                re = group.Email;
            } else {
                return null;
            }
        }
        return re;

    }

    public String wfGetGroupCCList(Long groupId) {
        String re = null;
        if (groupId != null) {
            Group group = groupRepo.findOne(groupId);
            if (group != null && group.Email != null) {
                re = group.ccList;
            } else {
                return null;
            }
        }
        return re;

    }

    /**
     * @param emp_id
     * @return
     */
    public EmployeeInfo getMentionedEmployee(Long emp_id) {
        EmployeeInfo re = new EmployeeInfo();
        if (emp_id != null) {
            Employee emp = employeeRepo.findOne(emp_id);
            if (emp != null) {
                re.ContractTypeID = emp.ContractTypeID;
                re.CORPORATE_ID = emp.CORPORATE_ID;
                re.DeptID = emp.DeptID;
                re.Email = emp.Email;
                re.Emp_ID = emp.Emp_ID;
                re.Expire_Date = emp.Expire_Date;
                re.HourCost = emp.HourCost;
                re.image = emp.image;
                re.Mobile_Phone = emp.Mobile_Phone;
                re.Name = emp.Name;
                re.Name_First = emp.Name_First;
                re.Name_Last = emp.Name_Last;
                re.Password = emp.Password;
                re.startWorkingHour = emp.startWorkingHour;
                re.WorkingDays = emp.WorkingDays;
                re.User_Name = emp.User_Name;
                re.statusName = emp.statusName;
                re.workinghours = emp.workinghours;
                re.svgFlag = emp.svgFlag;
                if (emp.PARENT_ID != null)
                    re.PARENT_ID = emp.PARENT_ID;
            } else {
                throw new NTGRestException("", "User is Not Found");
            }
        }
        return re;

    }

    public EmployeeInfo getEmployeeByMail(String mail) {
        EmployeeInfo re = new EmployeeInfo();
        if (mail != null) {
            Employee emp = employeeRepo.findByMail(mail);
            if (emp != null) {

                re = ObjectMapperUtils.map(emp, re);


            } else {
                throw new NTGRestException("", "User is Not Found");
            }
        }
        return re;
    }

    // added By Mahmoud to get Manager Employees
    public UserResponse GetManagerEmployees(Long managerId) {
        List<EmployeeInfo> EmpInfoList = new ArrayList<>();
        UserResponse res = new UserResponse();

        if (managerId != null) {
            // Edited by Asmaa
            String queryString = null;
            if (sqlHelper.getConnectionType() == 1) {

                queryString = "WITH ManagerEmployees ( recid, parent_id, name, rowlevel ) AS ( SELECT c.recid, c.parent_id, c.name, 1 AS rowlevel FROM "
                        + " comp_employee c WHERE c.recid = " + managerId
                        + " UNION ALL SELECT t.recid, t.parent_id, t.name, me.rowlevel + 1 rowlevel FROM "
                        + " comp_employee t JOIN ManagerEmployees me ON t.parent_id = me.recid ) SELECT m.recid, m.parent_id, m.name FROM ManagerEmployees m "
                        + " ORDER BY m.recid";

            } else {

                queryString = "WITH RECURSIVE ManagerEmployees ( recid, parent_id, name, rowlevel ) AS ( SELECT c.recid, c.parent_id, c.name, 1 AS rowlevel FROM "
                        + " comp_employee c WHERE c.recid = " + managerId
                        + " UNION ALL SELECT t.recid, t.parent_id, t.name, me.rowlevel + 1 rowlevel FROM "
                        + " comp_employee t JOIN ManagerEmployees me ON t.parent_id = me.recid ) SELECT m.recid, m.parent_id, m.name FROM ManagerEmployees m "
                        + " ORDER BY m.recid";

            }
            List<Map<String, Object>> empList = jdbcTemplate.queryForList(queryString);

            if (empList != null && empList.size() > 0) {
                for (Map<String, Object> emp : empList) {
                    EmployeeInfo empInfo = new EmployeeInfo();

                    empInfo.Emp_ID = Long.parseLong(emp.get("recid").toString());
                    empInfo.Name = emp.get("name").toString();
                    empInfo.PARENT_ID = Long.parseLong(emp.get("parent_id").toString());
                    //Dev-00000414 :  Fix My Team Tree to have corect main Parent
                    if (empInfo.Emp_ID == managerId) {
                        empInfo.PARENT_ID = -1;
                    }
                    EmpInfoList.add(empInfo);

                }
                if (EmpInfoList != null && EmpInfoList.size() > 0) {
                    res.returnValue = EmpInfoList.toArray(new EmployeeInfo[EmpInfoList.size()]);
                }
            } else {
                throw new NTGRestException("", "User is Not Found");
            }
        }
        return res;

    }

    // added by Asmaa to get Employees of list of Managers
    public UserResponse GetManagersEmployees(ArrayList<Long> managerIds) {
        List<EmployeeInfo> EmpInfoList = new ArrayList<>();
        UserResponse res = new UserResponse();

        if (managerIds.size() > 0) {

            String queryString = null;
            if (sqlHelper.getConnectionType() == 1) {

                queryString = "WITH ManagerEmployees ( recid, parent_id, name, rowlevel ) AS ( SELECT c.recid, c.parent_id, c.name, 1 AS rowlevel FROM "
                        + " comp_employee c WHERE c.parent_id in (%s)"
                        + " UNION ALL SELECT t.recid, t.parent_id, t.name, me.rowlevel + 1 rowlevel FROM "
                        + " comp_employee t JOIN ManagerEmployees me ON t.parent_id = me.recid ) SELECT m.recid, m.parent_id, m.name FROM ManagerEmployees m "
                        + " ORDER BY m.recid";

            } else {

                queryString = "WITH RECURSIVE ManagerEmployees ( recid, parent_id, name, rowlevel ) AS ( SELECT c.recid, c.parent_id, c.name, 1 AS rowlevel FROM "
                        + " comp_employee c WHERE c.parent_id in (%s)"
                        + " UNION ALL SELECT t.recid, t.parent_id, t.name, me.rowlevel + 1 rowlevel FROM "
                        + " comp_employee t JOIN ManagerEmployees me ON t.parent_id = me.recid ) SELECT m.recid, m.parent_id, m.name FROM ManagerEmployees m "
                        + " ORDER BY m.recid";
            }

            String inParams = String.join(",", managerIds.stream().map(id -> "?").collect(Collectors.toList()));
            List<Map<String, Object>> empList = jdbcTemplate.queryForList(String.format(queryString, inParams), managerIds.toArray());

            if (empList != null && empList.size() > 0) {
                for (Map<String, Object> emp : empList) {
                    EmployeeInfo empInfo = new EmployeeInfo();

                    empInfo.Emp_ID = Long.parseLong(emp.get("recid").toString());
                    empInfo.Name = emp.get("name").toString();
                    empInfo.PARENT_ID = Long.parseLong(emp.get("parent_id").toString());

                    EmpInfoList.add(empInfo);

                }
                if (EmpInfoList != null && EmpInfoList.size() > 0) {
                    EmpInfoList.get(0).PARENT_ID = 0;
                    res.returnValue = EmpInfoList.toArray(new EmployeeInfo[EmpInfoList.size()]);
                }
            } else {
                throw new NTGRestException("", "User is Not Found");
            }
        }
        return res;

    }

    // added by Abdulrahman Updated By Mahmoud
    // to retrieve manager using empId and manager level
    public UserResponse GetEmployeeMangers(Long empId, Long managerLevel) {
        List<EmployeeInfo> EmpInfoList = new ArrayList<>();
        UserResponse userResponse = new UserResponse();
        StringBuilder queryString = new StringBuilder();

        if (Utils.isNotEmpty(empId) && Utils.isNotEmpty(managerLevel)) {

            queryString.append("WITH ");
            queryString.append(sqlHelper.getConnectionType() > 1 ? "RECURSIVE " : "");
            queryString.append("employeeManagers  ( recid, parent_id, name, email, image, rowlevel ) ");
            queryString.append("AS ( SELECT c.recid, c.parent_id, c.name, c.email, c.image, 0 AS rowlevel FROM ");
            queryString.append(" comp_employee c WHERE c.recid = " + empId);
            queryString.append(
                    " UNION ALL SELECT t.recid, t.parent_id, t.name,t.email, t.image, em.rowlevel + 1 rowlevel FROM ");
            queryString.append(" comp_employee t JOIN employeeManagers em ON t.recid = em.parent_id ) ");
            queryString.append(" SELECT e.recid, e.parent_id, e.name, e.email, e.image FROM employeeManagers e ");
            queryString.append(" WHERE e.rowlevel = " + managerLevel + " ORDER BY e.recid");

            List<Map<String, Object>> empList = jdbcTemplate.queryForList(queryString.toString());

            if (Utils.isNotEmpty(empList)) {
                for (Map<String, Object> emp : empList) {
                    EmployeeInfo employeeInfo = new EmployeeInfo();

                    employeeInfo.Emp_ID = Long.parseLong(emp.get("recid").toString());
                    employeeInfo.Name = emp.get("name").toString();
                    employeeInfo.Email = emp.get("email").toString();
                    employeeInfo.image = (byte[]) emp.get("image");
                    if (emp.get("parent_id") != null)
                        employeeInfo.PARENT_ID = Long.parseLong(emp.get("parent_id").toString());

                    EmpInfoList.add(employeeInfo);
                }
                if (Utils.isNotEmpty(EmpInfoList)) {
                    userResponse.returnValue = EmpInfoList.toArray(new EmployeeInfo[EmpInfoList.size()]);
                }
            }
        } else {
            throw new NTGRestException("", "User is Not Found OR User Have No Managers");
        }
        return userResponse;

    }

    public GetEmailsResponse wf_GetEmployeesEmails(List<Long> employees_id) {
        GetEmailsResponse emails = new GetEmailsResponse();
        List<String> employeeEmails = new ArrayList<String>();

        for (Long emp_id : employees_id) {
            if (emp_id != null) {
                Employee emp = employeeRepo.findOne(emp_id);
                if (Utils.isNotEmpty(emp) && Utils.isNotEmpty(emp.Email)) {
                    employeeEmails.add(emp.Email);
                }
            }
        }
        emails.setEmails(employeeEmails);
        return emails;
    }


    /**
     * @throws Exception
     * @EventListener(ApplicationReadyEvent.class) on application is ready
     * convert encrypted passwords in db to hashing using Bcrypt by decrypting and then hashing
     * Hashing using Bcrypt starting with '$2a$' as 2a is the version used
     * @author babdelaziz
     */

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    public void passwordEncryptionToHashing() throws Exception {
        List<Employee> employeeList = (List<Employee>) employeeRepo.findByPasswordNotLike("$2a$%");
        for (Employee employee : employeeList) {
            if (employee.Password != null) {

                String method;
                String encryptionKey;
                encryptionKey = configurationEntityService.getByKey(ConfigurationConstant.PASS_ENC_KEY);
                method = configurationEntityService.getByKey(ConfigurationConstant.PASS_ENC_METHOD);
                String plainTextPass = null;
                try {
                    plainTextPass = EncryptionUtils.decrypt(employee.Password, "030385_" + encryptionKey, method);
                } catch (Exception e) {
                    System.out.println("War: Fail user " + employee.getUser_Name() + " Migration Issue for Password set to P@ssw0rd");
                    plainTextPass = "P@ssw0rd";
                }
//                long t = System.currentTimeMillis();
                String hashedPassword = passwordEncoder.encode(plainTextPass);
//                t = System.currentTimeMillis() - t ;
//                System.out.println("-> Enc in " + t );
                employee.setPassword(hashedPassword);
                employee.Password = hashedPassword;
                String updateEmployee = "update comp_employee set password = '" + hashedPassword + "' " +
                        "where recid = " + employee.getEmp_ID();
                jdbcTemplate.update(updateEmployee);
            }
        }
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void updateEmployeeParentId(Map request) {

        List<Long> employees_id = null;
        Long parentId = null;
        Object employeeIdsObjects = request.get("employeeIds");
        Object managerIdObjects = request.get("managerId");
        if (employeeIdsObjects != null) {
            employees_id = (List<Long>) employeeIdsObjects;
        }
        if (employeeIdsObjects != null) {
            parentId = Long.parseLong(managerIdObjects.toString());
        }

        employeeRepo.updateEmployeeParentId(employees_id, parentId);
    }


    public AddGroupResponse addTenantUser(TenantGroupsUserRequest req, Boolean encryptPassword) throws Exception {
        AddGroupResponse response = new AddGroupResponse();
        Map<String, Object> userGroupIds = addUserAndGroups(req, encryptPassword);
        // add user and group relation.
        if (Utils.isNotEmpty(userGroupIds) && Utils.isNotEmpty(userGroupIds.get("userId")) && Utils.isNotEmpty(userGroupIds.get("groupId"))) {
            long userId = Long.parseLong(userGroupIds.get("userId").toString());
            long groupId = Long.parseLong(userGroupIds.get("groupId").toString());
            GroupMembersRequest grpMemReq = new GroupMembersRequest();
            grpMemReq.LoginUserInfo = req.getLoginUserInfo();
            grpMemReq.UserId = userId;
            grpMemReq.GroupId = groupId;
            AddUserToGroup(grpMemReq);
        }
        // clone configration for new teanant
        cloneConfigrationForNewTenant(req.getCompanyName());

        // reset tenant company name
        TenantContext.setCompanyName(req.getOldTenant());

        return response;
    }

    private void cloneConfigrationForNewTenant(String companyName) {
        TenantContext.setCompanyName(companyName);
        List<Configuration> globalConfig = configurationRepository.findByGlobalTenant();
        if (Utils.isNotEmpty(globalConfig)) {
            List<Configuration> newConfig = new ArrayList<>();
            for (Configuration config : globalConfig) {
                Configuration newCon = new Configuration();
                newCon.setKey(config.getKey());
                newCon.setNote(config.getNote());
                newCon.setValue(config.getValue());
                newCon.setGlobalTenant(false);
                newConfig.add(newCon);
            }
            configurationRepository.saveAll(newConfig);
        }
    }

    @Transactional
    private Map<String, Object> addUserAndGroups(TenantGroupsUserRequest req, Boolean encryptPassword) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (Utils.isNotEmpty(req)) {
            if (Utils.isNotEmpty(req.getGroups())) {
                Group company = req.getGroups().stream().filter(c -> c.gruop_type == 1).findAny().orElse(null);
                AddGroupResponse companyRes = createTenantGroup(req, company);
                if (Utils.isNotEmpty(companyRes) && Utils.isNotEmpty(companyRes.getReturnValue())) {
                    Group org = req.getGroups().stream().filter(c -> c.gruop_type == 2).findAny().orElse(null);
                    org.PARENT_GROUP_ID = Long.parseLong(companyRes.getReturnValue());
                    AddGroupResponse orgRes = createTenantGroup(req, org);
                    if (Utils.isNotEmpty(orgRes) && Utils.isNotEmpty(orgRes.getReturnValue())) {
                        Group group = req.getGroups().stream().filter(c -> c.gruop_type == 3).findAny().orElse(null);
                        group.PARENT_GROUP_ID = Long.parseLong(orgRes.getReturnValue());
                        AddGroupResponse groupRes = createTenantGroup(req, group);
                        if (Utils.isNotEmpty(groupRes)) {
                            AddEmployeeRequest empReq = new AddEmployeeRequest();
                            empReq.companyName = req.getCompanyName();
                            empReq.emp = req.getEmp();
                            StringResponse empRes = CRMAddEmployee(empReq, encryptPassword);
                            if (Utils.isNotEmpty(empRes)) {
                                map.put("userId", empRes.returnValue);
                                map.put("groupId", groupRes.getReturnValue());
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    private AddGroupResponse createTenantGroup(TenantGroupsUserRequest req, Group group) throws Exception {
        AddGroupRequest request = new AddGroupRequest();
        request.companyName = req.getCompanyName();
        request.LoginUserInfo = req.getLoginUserInfo();
        request.group = group;
        return createGroup(request);
    }

    // Enhancment of users Profile page @Ekhaled
    public List<EmployeeInfo> mapEmployeeResponse(List<Employee> newEmpList) {

        ArrayList<EmployeeInfo> returnValue = new ArrayList<>();

        for (Employee emp : newEmpList) {
            if (emp.getUserType() == null || emp.getUserType() != CommonConstants.USER_TYPE_SYSTEM) {
                EmployeeInfo empInfo = new EmployeeInfo();
                empInfo.ContractTypeID = (emp.ContractTypeID == null) ? -1 : emp.ContractTypeID;
                empInfo.CORPORATE_ID = emp.CORPORATE_ID;
                empInfo.DeptID = (emp.DeptID == null) ? -1 : emp.DeptID;
                empInfo.Email = emp.Email;
                empInfo.Emp_ID = emp.Emp_ID;
                empInfo.Expire_Date = emp.Expire_Date;
                empInfo.HourCost = (emp.HourCost == null) ? 0 : emp.HourCost;
                empInfo.image = emp.image;
                empInfo.locationName = emp.locationName;
                empInfo.loginType = (emp.loginType == null) ? 0 : emp.loginType;
                empInfo.Mobile_Phone = emp.Mobile_Phone;
                empInfo.Name = emp.Name;
                empInfo.Name_First = emp.Name_First;
                empInfo.Name_Last = emp.Name_Last;
                empInfo.PARENT_ID = (emp.PARENT_ID == null) ? -1 : emp.PARENT_ID;
                empInfo.Prefered_Language = emp.Prefered_Language;
                empInfo.SmallImage = emp.SmallImage;
                empInfo.startWorkingHour = emp.startWorkingHour;
                empInfo.Status_ID = emp.Status_ID;
                empInfo.statusName = Utils.isNotEmpty(emp.statusName) ? StringUtils.capitalize(emp.statusName) : "";
                empInfo.User_Name = emp.User_Name;
                empInfo.WorkingDays = emp.WorkingDays;
                empInfo.gender = emp.gender;
                empInfo.Skills = emp.Skills;
                empInfo.workinghours = (emp.workinghours == null) ? 8 : emp.workinghours;
                returnValue.add(empInfo);
            }
        }
        return returnValue;
    }

    // Enhancment of users Profile page @Ekhaled
    private void GetEmployeeListOfLoginUSer(List<Employee> newEmpList, long managerId) {

        List<Employee> childrenList = employeeRepo.findAllChildrenOfEmployee(managerId);

        if (Utils.isNotEmpty(childrenList)) {
            for (Employee emp : childrenList) {
                newEmpList.add(emp);
                GetEmployeeListOfLoginUSer(newEmpList, emp.Emp_ID);

            }
        }
    }

    // Enhancment of users Profile page @Ekhaled
    public UserResponse getEmployeeByUsername(HashMap<String, String> targetUsers) {


        UserResponse response = new UserResponse();

        String name = targetUsers.get("FulName").toLowerCase();
        String email = targetUsers.get("Email").toLowerCase();
        String userName = targetUsers.get("username").toLowerCase();

        List<Employee> empList = employeeRepo.findByUserNameAndEmailAndName(userName, name, email);

        List<EmployeeInfo> returnedd = mapEmployeeResponse(empList);

        response.returnValue = (EmployeeInfo[]) returnedd.toArray(new EmployeeInfo[returnedd.size()]);

        return response;

    }

    public Employee GetLoginEmployee(UserRequest req) {
        return employeeRepo.findOne(Long.parseLong(req.LoginUserInfo.userID));
    }

    public UserResponse getUsersInformationListInfo(HashMap<String, Long> targetUser) {

        UserResponse response = new UserResponse();

        List<Employee> newEmpList = new ArrayList<Employee>();

        Employee emp = employeeRepo.findOne(targetUser.get("id"));
        newEmpList.add(emp);
        GetEmployeeListOfLoginUSer(newEmpList, targetUser.get("id"));
        Employee parent = employeeRepo.findOne(targetUser.get("parent_id"));
        if (parent != null) {
            newEmpList.add(parent);
        }

        if (Utils.isNotEmpty(newEmpList)) {

            List<EmployeeInfo> returnedd = mapEmployeeResponse(newEmpList);
            response.returnValue = (EmployeeInfo[]) returnedd.toArray(new EmployeeInfo[returnedd.size()]);
        }

        return response;
    }

    // Incase of mapping user info from portal to pre made tenant
    public StringResponse updateEmployeeInfoFromPortal(HashMap<String, String> customerObj) {

        StringResponse res = new StringResponse();

        // login with teanant to install apps
        String email = (Utils.isNotEmpty(customerObj.get("email"))) ? customerObj.get("email").toString() : null;
        String userName = (Utils.isNotEmpty(customerObj.get("userName"))) ? customerObj.get("userName").toString() : null;
        String firstName = (Utils.isNotEmpty(customerObj.get("firstName"))) ? customerObj.get("firstName").toString() : null;
        String lastName = (Utils.isNotEmpty(customerObj.get("lastName"))) ? customerObj.get("lastName").toString() : null;
        String companyName = (Utils.isNotEmpty(customerObj.get("companyName"))) ? customerObj.get("companyName").toString() : null;
        String defaultUserTobeReplaced = (Utils.isNotEmpty(customerObj.get("defaultUserTobeReplaced"))) ? customerObj.get("defaultUserTobeReplaced").toString() : null;

        //TenantContext.setCurrentTenantByCompanyName(companyName, "", "");
        List<Employee> employees = employeeRepo.findEmployeeByCompanyNameAndUserName(companyName, defaultUserTobeReplaced);
        if(employees.size() > 0) {
            Employee employee = employees.get(0);
            employee.setEmail(email);
            employee.setUser_Name(userName);
            employee.setName_First(firstName);
            employee.setName_Last(lastName);
            employee.setName(firstName + " " + lastName);
            employeeRepo.save(employee);
            res = new StringResponse("Updated Successfully!");
        } else {
            res =  new StringResponse("No user found!");
        }
        return res;
    }
}
