package com.ntg.sadmin.web.services;

import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.common.NTGMessageOperation;
import com.ntg.sadmin.constants.CodesAndKeys;
import com.ntg.sadmin.constants.CommonConstants;
import com.ntg.sadmin.constants.ConfigurationConstant;
import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.entities.Group;
import com.ntg.sadmin.data.entities.SaLoginUser;
import com.ntg.sadmin.data.entities.SaLoginUserHistory;
import com.ntg.sadmin.data.repositories.SaLoginUserHistoryRepository;
import com.ntg.sadmin.data.repositories.SaLoginUserRepository;
import com.ntg.sadmin.data.service.ConfigurationEntityService;
import com.ntg.sadmin.data.service.EmployeeEntityService;
import com.ntg.sadmin.data.service.GroupEntityService;
import com.ntg.sadmin.exceptions.BusinessException;
import com.ntg.sadmin.exceptions.NTGRestException;
import com.ntg.sadmin.utils.EncryptionUtils;
import com.ntg.sadmin.utils.LdapUtils;
import com.ntg.sadmin.utils.Utils;
import com.ntg.sadmin.web.dto.*;
import com.ntg.sadmin.web.requests.GetToDoListRequest;
import com.ntg.sadmin.web.requests.LoginUserRequest;
import com.ntg.sadmin.web.requests.LogoutRequest;
import com.ntg.sadmin.web.requests.UserCompaniesListRequest;
import com.ntg.sadmin.web.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author abdelrahman
 */
@Service
public class ITTSAPIsService {

    private static final Logger logger = LoggerFactory.getLogger(ITTSAPIsService.class);

    @Autowired
    private EmployeeEntityService employeeEntityService;

    @Autowired
    private GroupEntityService groupRepo;

    @Autowired
    private SaLoginUserRepository saLoginUserRepo;

    @Autowired
    private SaLoginUserHistoryRepository historyRepo;

    @Autowired
    private ConfigurationEntityService configurationEntityService;

    @Autowired
    private EncryptionUtils encryptionUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LdapUtils ldapUtils;
    /**
     * @param req
     * @return
     */

    public LoginUserResponse CheckSessionValidityInfo(LoginUserRequest req) {
        LoginUser response = new LoginUser();
        try {

            // Dev-00003536:logging with Demo link mobile requirments
            if(req.companyName!=null) {
                TenantContext.setCompanyName(req.companyName);
            }
            else{
                TenantContext.setCompanyName(req.LoginUserInfo.companyName);
            }

            LoginUserInfo userInfo = new LoginUserInfo();
            SessionInfo loginUserInfo = req.LoginUserInfo;

            SaLoginUser saLoginUser = saLoginUserRepo.findBySessionID(loginUserInfo.sessionID);
            if (saLoginUser == null) {
                throw new BusinessException(CodesAndKeys.SESSION_EXPIRED_CODE, CodesAndKeys.SESSION_EXPIRED_KEY, CodesAndKeys.SESSION_EXPIRED_MESSAGE);
            }
            if (!saLoginUser.userID.equals(req.LoginUserInfo.userID)) {
                throw new BusinessException(CodesAndKeys.INVALID_SESSION_ID_CODE, CodesAndKeys.INVALID_SESSION_ID_KEY, CodesAndKeys.INVALID_SESSION_ID_MESSAGE);
            }
            Employee emp = employeeEntityService.findEmployeeById(Long.parseLong(saLoginUser.userID));
            if (emp == null) {
                throw new BusinessException(CodesAndKeys.INVALID_SESSION_ID_CODE, CodesAndKeys.INVALID_SESSION_ID_KEY, CodesAndKeys.INVALID_SESSION_ID_MESSAGE);
            }
            //username is case-insensitive
            if (!saLoginUser.loginUserName.equalsIgnoreCase(emp.User_Name)
                    || !saLoginUser.userID.equals(emp.Emp_ID.toString())) {
                throw new BusinessException(CodesAndKeys.SESSION_EXPIRED_CODE, CodesAndKeys.SESSION_EXPIRED_KEY, CodesAndKeys.SESSION_EXPIRED_MESSAGE);
            }
            response.employeeId = emp.Emp_ID.toString();
            loginUserInfo.loginUserName = emp.User_Name;

            if (emp.userGroups.size() <= 0) {
                throw new NTGRestException("", "this user is not in any group");
            } else {
                response.UserGroups = new ArrayList<UserGroup>();
                for (Group group : emp.userGroups) {
                    if (group.isHaveAdminPrev = true)
                        response.isHaveAdminPrev = true;

                    UserGroup userGroup = new UserGroup();
                    userGroup.Group_Name = group.Name;
                    userGroup.GroupID = group.Group_ID;
                    userGroup.isdefault = (group.isDefault == null) ? false : group.isDefault;
                    userGroup.UserID = emp.Emp_ID;
                    response.UserGroups.add(userGroup);

                }
            }
            saLoginUserRepo.deleteBySessionID(loginUserInfo.sessionID);
            userInfo.EmpStatus = emp.Status_ID;

            userInfo.StartWorkingHour = emp.startWorkingHour;
            userInfo.WorkingHours = emp.workinghours;
            userInfo.expiredate = emp.Expire_Date;
            userInfo.fullName = emp.Name;
            userInfo.hourcost = emp.HourCost;
            userInfo.Image = emp.image;
            userInfo.Mail = emp.Email;
            userInfo.BranchName = emp.locationName;
            userInfo.WorkingDays = emp.WorkingDays;
            Random random = new Random();
            long sessionIdRandom = random.nextLong();
            loginUserInfo.sessionID = System.currentTimeMillis() + "-" + emp.Emp_ID + "-" + sessionIdRandom;
            userInfo.SessionInfo = loginUserInfo;
            response.MainLoginInfo = userInfo;
            // add the new session into login user table
            saLoginUser.recid = null;
            saLoginUser.sessionID = loginUserInfo.sessionID;
            saLoginUserRepo.save(saLoginUser);

            LoginUserResponse res = new LoginUserResponse();
            res.loginUser = response;
            return res;

        } catch (NTGRestException e) {
            NTGMessageOperation.PrintErrorTrace(e);
            response.restException = e;
            LoginUserResponse res = new LoginUserResponse();
            res.loginUser = response;
            return res;
        }

    }

    public LoginUser LogUser(LoginUserRequest req) {
        LoginUser response = new LoginUser();
        try {
            // set company from request
            TenantContext.setCompanyName(req.companyName);

            validateLoginRequest(req);
            Employee emp = loadUserAndVerifyLogin(req);
            response = buildLoginResponse(req, emp);
            return response;
        } catch (NTGRestException e) {
            NTGMessageOperation.PrintErrorTrace(e);
            response.restException = e;
            return response;
        }
    }

    // to sign out

    /**
     * @param req
     * @return
     */
    public StringResponse signOut(LogoutRequest req) {
        try {
            SaLoginUserHistory history = new SaLoginUserHistory();
            SaLoginUser loggedUser = saLoginUserRepo.findBySessionID(req.LoginUserInfo.sessionID);
            if (loggedUser == null) {
                return new StringResponse("login First");
            } else {
                history.companyId = loggedUser.companyId;
                history.companyName = loggedUser.companyName;
                history.loginDate = loggedUser.loginDate;
                history.loginTime = loggedUser.loginTime;
                history.loginUserName = loggedUser.loginUserName;
                history.logoutDate = new Date();
//				Time sqlDate = new java.sql.Time(history.logoutDate.getTime());
                // history.logoutTime = new Time();

                history.sessionID = loggedUser.sessionID;
                history.userID = loggedUser.userID;
                history.transactionID = loggedUser.transactionID;
                historyRepo.save(history);
                saLoginUserRepo.deleteById(loggedUser.recid);
                return new StringResponse("you logged out successfully");
            }

        } catch (Exception e) {
            StringResponse res = new StringResponse();
            res.setRestException((NTGRestException) e);
            return res;
        }

    }

    /**
     * @param req
     * @return
     */
    public GetToDoListResponse GetToDoList(GetToDoListRequest req) {

        // set company from request
        TenantContext.setCompanyName(req.companyName);

        return new GetToDoListResponse();
    }

    /**
     * @return
     */
    public GetUserMessagesResponse GetUserMessagesList() {
        return new GetUserMessagesResponse();
    }

    /**
     * @param req
     * @return
     */
    public UserCompaniesListResponse getUserCompaniesList(UserCompaniesListRequest req) {

        UserCompaniesListResponse res = new UserCompaniesListResponse();
        ArrayList<UserCompaniesList> returnValue = new ArrayList<UserCompaniesList>();

        try {
            // set company from request
            TenantContext.setCompanyName(req.companyName);

            Employee emp = employeeEntityService.findOne(Long.parseLong(req.LoginUserInfo.userID));
            if (emp == null) {
                throw new NTGRestException("", "this Employee doesnt Exist!");
            }

            if (emp.userGroups.size() <= 0) {
                throw new NTGRestException("", "this Employee not in any Group!");
            } else {

                for (Group userGroup : emp.userGroups) {
                    UserCompaniesList mappedGroup = new UserCompaniesList();

                    mappedGroup.Name = userGroup.Name;
                    mappedGroup.Group_ID = userGroup.Group_ID;
                    // mappedGroup.UserID=emp.Emp_ID;
                    mappedGroup.gruop_type = userGroup.gruop_type;
                    returnValue.add(mappedGroup);

                    if (userGroup.PARENT_GROUP_ID != null) {
                        Group Organization = new Group();
                        Organization = groupRepo.findOne(userGroup.PARENT_GROUP_ID);
                        UserCompaniesList mappedOrganization = new UserCompaniesList();
                        mappedOrganization.Name = Organization.Name;
                        mappedOrganization.Group_ID = Organization.Group_ID;
                        // mappedGroup.UserID=emp.Emp_ID;
                        mappedOrganization.gruop_type = Organization.gruop_type;
                        returnValue.add(mappedOrganization);

                        if (Organization.PARENT_GROUP_ID != null) {
                            Group Company = new Group();
                            Company = groupRepo.findOne(Organization.PARENT_GROUP_ID);
                            UserCompaniesList mappedCompany = new UserCompaniesList();
                            mappedCompany.Name = Company.Name;
                            mappedCompany.Group_ID = Company.Group_ID;
                            // mappedGroup.UserID=emp.Emp_ID;
                            mappedCompany.gruop_type = Company.gruop_type;
                            returnValue.add(mappedCompany);
                        }
                    }

                }
                res.returnValue = (UserCompaniesList[]) returnValue.toArray(new UserCompaniesList[returnValue.size()]);
                return res;
            }
        } catch (NTGRestException e) {
            NTGMessageOperation.PrintErrorTrace(e);
            throw e;
            // res.restException = e;
            // return res;

        }
    }

    private Employee createNewUserFirstTime(String username, String password, String email, int loginType, Employee ldapUserInfo ) {
        try {

            //gender, location Name, userName, Email, firstName, lastName, mobile, image
            Employee newEmployee = new Employee();
            newEmployee.ContractTypeID = 1L;
            newEmployee.Email = email;
            newEmployee.HourCost = 100d;
            newEmployee.Mobile_Phone = "";
            newEmployee.Name = username;
            newEmployee.Name_First = username.split("@")[0];
            newEmployee.Name_Last = newEmployee.Name_First;
            newEmployee.Password = null;
            newEmployee.Prefered_Language = "Eng";
            newEmployee.Status_ID = CommonConstants.ACCOUNT_ACTIVATED;
            newEmployee.User_Name = username.split("@")[0];
            newEmployee.WorkingDays = "1,2,3,4,5";
            newEmployee.gender = "male";
            newEmployee.locationName = "Egypt";
            newEmployee.statusName = "active";
            newEmployee.CORPORATE_ID = "0";
            newEmployee.DeptID = 0L;
            newEmployee.PARENT_ID = 0L;
            newEmployee.loginType = (long) loginType;
            newEmployee.workinghours = 9L;
            newEmployee.startWorkingHour = null;

            if(Utils.isNotEmpty(ldapUserInfo)){
                newEmployee.User_Name = (ldapUserInfo.getUser_Name() != null)? ldapUserInfo.getUser_Name() : newEmployee.User_Name;
                newEmployee.Name_First = (ldapUserInfo.getName_First() != null)?ldapUserInfo.getName_First() : newEmployee.Name_First;
                newEmployee.Name_Last = (ldapUserInfo.getName_Last() != null)? ldapUserInfo.getName_Last() : newEmployee.Name_Last;
                newEmployee.gender = (ldapUserInfo.getGender() != null)? ldapUserInfo.getGender() : newEmployee.gender;
                newEmployee.Mobile_Phone = (ldapUserInfo.getMobile_Phone() != null)? ldapUserInfo.getMobile_Phone() : newEmployee.Mobile_Phone;
                newEmployee.locationName = (ldapUserInfo.getLocationName() != null)? ldapUserInfo.getLocationName() : newEmployee.locationName;
                newEmployee.image = (ldapUserInfo.getImage() != null)? ldapUserInfo.getImage() : newEmployee.image;
                newEmployee.Email = (ldapUserInfo.getEmail() != null)? ldapUserInfo.getEmail(): newEmployee.Email;
            }

            employeeEntityService.save(newEmployee);

            Group group = groupRepo.findOne(Long.parseLong(configurationEntityService.getByKey(ConfigurationConstant.LDAP_DEFAULT_GROUP)));
            group.groupUsers.add(newEmployee);
            groupRepo.save(group);

            newEmployee.userGroups = new ArrayList<>();
            newEmployee.userGroups.add(group);
            return newEmployee;
        } catch (Exception e) {
            NTGMessageOperation.PrintErrorTrace(e);
            throw new NTGRestException("", "Can't create new user form ldap");
        }

    }

    private void validateLoginRequest(LoginUserRequest request) {
        logger.debug("start validateLoginRequest function");
        SessionInfo loginUserInfo = request.LoginUserInfo;

        if (loginUserInfo == null) {
            throw new NTGRestException("", "Invalid Session Info");
        }
        if (loginUserInfo.companyName == null) {
            throw new NTGRestException("", "invalid Company Name");
        }
        logger.debug("end validateLoginRequest function");
    }

    private Employee loadUserAndVerifyLogin(LoginUserRequest request) {
        boolean enableRecaptcha = request.enableRecaptcha;
        logger.debug("start loadUserAndVerifyLogin function");
        SessionInfo loginUserInfo = request.LoginUserInfo;
        Long maxLoginAttempts = Long.parseLong(configurationEntityService.getByKey(ConfigurationConstant.MAX_FAILED_LOGIN));

        Employee employee = employeeEntityService.findByUsername(loginUserInfo.loginUserName);
        if (Utils.isEmpty(employee)) {
            employee = employeeEntityService.findByUsernameAndIsSuperAdmin(loginUserInfo.loginUserName);
        }

        int defaultLoginType = Integer
                .parseInt(configurationEntityService.getByKey(ConfigurationConstant.DEFAULT_LOGIN_TYPE));


        if (employee != null) {//Employee Found in Repository
            // Check Password Expiration
            if (employee.loginType == 0 && employee.getExpire_Date() != null && employee.getExpire_Date().before(new Date()))
                throw new BusinessException(CodesAndKeys.PASSWORD_EXPIRED_CODE, CodesAndKeys.PASSWORD_EXPIRED_KEY,
                        CodesAndKeys.PASSWORD_EXPIRED_MESSAGE);
                // Check Employee Status_ID
            else if (employee.Status_ID.equals(CommonConstants.ACCOUNT_SUSPENDED)) {// Account Suspended
                throw new BusinessException(CodesAndKeys.ACCOUNT_SUSPENDED_CODE, CodesAndKeys.ACCOUNT_SUSPENDED_KEY,
                        CodesAndKeys.ACCOUNT_SUSPENDED_MESSAGE);
            } else if (employee.Status_ID.equals(CommonConstants.ACCOUNT_TERMINATED)) {// Account Terminated
                throw new BusinessException(CodesAndKeys.ACCOUNT_TERMINATED_CODE, CodesAndKeys.ACCOUNT_TERMINATED_KEY,
                        CodesAndKeys.ACCOUNT_TERMINATED_MESSAGE);
            } else if (!enableRecaptcha && employee.Status_ID.equals(CommonConstants.ACCOUNT_TEMPORARY_LOCKED)) {// Account
                // Temporary
                // Locked
                // Check Temporary Lock and Re-Activate if Account Locking Period Ended
                if (checkReleaseTemporaryLock(employee)) {
                    employeeEntityService.handleEmployeeFailedLogin(employee, false, false,
                            CommonConstants.ACCOUNT_ACTIVATED);
                } else {
                    throw new BusinessException(CodesAndKeys.ACCOUNT_TEMPORARY_LOCKED_CODE,
                            CodesAndKeys.ACCOUNT_TEMPORARY_LOCKED_KEY, CodesAndKeys.ACCOUNT_TEMPORARY_LOCKED_MESSAGE);
                }
            }
            try {
                //Check Employee Login_Type and then Check Password and Login
                if (employee.loginType == CommonConstants.LOGIN_TYPE_DB) {
                    if (!passwordEncoder.matches(request.Password, employee.Password)) {
                        throw new BusinessException(CodesAndKeys.INVALID_USERNAME_PASSWORD_CODE, CodesAndKeys.INVALID_USERNAME_PASSWORD_KEY, CodesAndKeys.INVALID_USERNAME_PASSWORD_MESSAGE);
                    }
                } else if (employee.loginType == CommonConstants.LOGIN_TYPE_LDAP) {
                    String ldapDomain = configurationEntityService.getByKey(ConfigurationConstant.LDAP_DOMAIN);
                    String ldapUrl = configurationEntityService.getByKey(ConfigurationConstant.LDAP_URL);
                    ldapUtils.isLdapRegistered(ldapDomain, ldapUrl, loginUserInfo.loginUserName, request.Password);
                } else if (employee.loginType == CommonConstants.LOGIN_TYPE_SMTP) {
                    String emailUser = loginUserInfo.loginUserName;
                    String mailHost = configurationEntityService.getByKey(ConfigurationConstant.MAIL_HOST);
                    String mailPort = configurationEntityService.getByKey(ConfigurationConstant.MAIL_PORT);
                    String sslEnabled = configurationEntityService.getByKey(ConfigurationConstant.MAIL_PORT_SSL);
                    String userDomain = configurationEntityService.getByKey(ConfigurationConstant.MAIL_DOMAIN);

                    if (Utils.isNotEmpty(userDomain)) {
                        emailUser = emailUser + (userDomain.trim().startsWith("@") ? userDomain : ("@" + userDomain));
                    }


                    ldapUtils.authenticatedMailSMTP(mailHost, mailPort, emailUser, request.Password, ((Utils.isNotEmpty(sslEnabled) && sslEnabled.equalsIgnoreCase("true")) ? true : false));
                }
            } catch (BusinessException ex) {
                if (enableRecaptcha) {
                    if (employee.getFailedLoginCounter() >= maxLoginAttempts - 1) {
                        throw new BusinessException(CodesAndKeys.NUMBER_OF_FAILED_LOGINS_EXCEEDED_CODE, CodesAndKeys.NUMBER_OF_FAILED_LOGINS_EXCEEDED_KEY, CodesAndKeys.NUMBER_OF_FAILED_LOGINS_EXCEEDED_MESSAGE);
                    } else {
                        employeeEntityService.handleEmployeeFailedLoginForRecaptcha(employee);
                        throw new BusinessException(CodesAndKeys.INVALID_USERNAME_PASSWORD_CODE, CodesAndKeys.INVALID_USERNAME_PASSWORD_KEY, CodesAndKeys.INVALID_USERNAME_PASSWORD_MESSAGE);
                    }
                } else {
                    employeeFailedLogin(employee);
                }
            }

        } else {//If Employee is not in Repository,
            if (defaultLoginType == CommonConstants.LOGIN_TYPE_DB) {
                throw new BusinessException(CodesAndKeys.INVALID_USERNAME_PASSWORD_CODE, CodesAndKeys.INVALID_USERNAME_PASSWORD_KEY, CodesAndKeys.INVALID_USERNAME_PASSWORD_MESSAGE);
            } else if (defaultLoginType == CommonConstants.LOGIN_TYPE_LDAP) {
                String ldapDomain = configurationEntityService.getByKey(ConfigurationConstant.LDAP_DOMAIN);
                String ldapUrl = configurationEntityService.getByKey(ConfigurationConstant.LDAP_URL);

                boolean isLdapRegistered = ldapUtils.isLdapRegistered(ldapDomain, ldapUrl, loginUserInfo.loginUserName,
                        request.Password);
                if (isLdapRegistered) {
                    String baseDN = configurationEntityService.getByKey(ConfigurationConstant.BASE_DN);
                    Employee ldapUserInfo = ldapUtils.getLdapData(ldapDomain, ldapUrl, loginUserInfo.loginUserName, request.Password, baseDN,loginUserInfo.loginUserName);
                    return createNewUserFirstTime(loginUserInfo.loginUserName, request.Password, loginUserInfo.loginUserName, CommonConstants.LOGIN_TYPE_LDAP, ldapUserInfo);
                }
            } else if (defaultLoginType == CommonConstants.LOGIN_TYPE_SMTP) {
                String emailUser = loginUserInfo.loginUserName;
                String mailHost = configurationEntityService.getByKey(ConfigurationConstant.MAIL_HOST);
                String mailPort = configurationEntityService.getByKey(ConfigurationConstant.MAIL_PORT);
                String sslEnabled = configurationEntityService.getByKey(ConfigurationConstant.MAIL_PORT_SSL);
                String userDomain = configurationEntityService.getByKey(ConfigurationConstant.MAIL_DOMAIN);

                if (Utils.isNotEmpty(userDomain)) {
                    emailUser = emailUser + (userDomain.trim().startsWith("@") ? userDomain : ("@" + userDomain));
                }

                ldapUtils.authenticatedMailSMTP(mailHost, mailPort, emailUser, request.Password, ((Utils.isNotEmpty(sslEnabled) && sslEnabled.equalsIgnoreCase("true")) ? true : false));
                return createNewUserFirstTime(loginUserInfo.loginUserName, request.Password, loginUserInfo.loginUserName, CommonConstants.LOGIN_TYPE_SMTP, null);
            }
        }
        logger.debug("end loadUserAndVerifyLogin function");
        // Check Failed Login Counter and RE-SET It To Zero if login success
        if (enableRecaptcha && employee.getFailedLoginCounter() >= maxLoginAttempts - 1) {
            employee.setFailedLoginCounter(0L);
            employeeEntityService.save(employee);
        }
        return employee;
    }

    /**
     * Method to Compare:
     * Account Locking Period
     * and
     * Difference between (Current time - Account Locking Time)
     *
     * @param employee
     * @return boolean
     * @author Amr Khaled
     * @since 2020
     */
    private boolean checkReleaseTemporaryLock(Employee employee) {
        Date timeNow = new Date();
        int lockingPeriod = Integer.parseInt(configurationEntityService.getByKey(ConfigurationConstant.LOCKING_TIME_IN_MINUTES));
        return employee.getAccountTemporaryLockedStartTime() == null || (timeNow.getTime() - employee.getAccountTemporaryLockedStartTime().getTime() > lockingPeriod * 60 * 1000);
    }

    /**
     * Used to Handle User Failed Login, called when --> Wrong Password Entered
     *
     * @param employee
     * @returns void
     * @author Amr Khaled
     * @since 2020
     */
    private void employeeFailedLogin(Employee employee) {
        Long maxLoginAttempts = Long.parseLong(configurationEntityService.getByKey(ConfigurationConstant.MAX_FAILED_LOGIN));
        Long maxTemporaryLocks = Long.parseLong(configurationEntityService.getByKey(ConfigurationConstant.MAX_TEMPORARY_LOCKS));

        if (employee.getFailedLoginCounter() > maxLoginAttempts) {//Employee Reached Max un-Successful Login Attempts
            if (employee.getTemporaryLockCounter() > maxTemporaryLocks) {//Employee Reached Max Account Locks
                employeeEntityService.handleEmployeeFailedLogin(employee, false, false, CommonConstants.ACCOUNT_SUSPENDED);
                throw new BusinessException(CodesAndKeys.ACCOUNT_SUSPENDED_CODE, CodesAndKeys.ACCOUNT_SUSPENDED_KEY, CodesAndKeys.ACCOUNT_SUSPENDED_MESSAGE);
            } else {//Employee Didn't Reach Max Account Locks
                employeeEntityService.handleEmployeeFailedLogin(employee, false, true, CommonConstants.ACCOUNT_TEMPORARY_LOCKED);
                throw new BusinessException(CodesAndKeys.ACCOUNT_TEMPORARY_LOCKED_CODE, CodesAndKeys.ACCOUNT_TEMPORARY_LOCKED_KEY, CodesAndKeys.ACCOUNT_TEMPORARY_LOCKED_MESSAGE);
            }
        } else {
            employeeEntityService.handleEmployeeFailedLogin(employee, true, false, null);
            throw new BusinessException(CodesAndKeys.INVALID_USERNAME_PASSWORD_CODE, CodesAndKeys.INVALID_USERNAME_PASSWORD_KEY, CodesAndKeys.INVALID_USERNAME_PASSWORD_MESSAGE);
        }
    }

    private LoginUser buildLoginResponse(LoginUserRequest request, Employee emp) {
        logger.debug("start buildLoginResponse function");

        SessionInfo LoginUserInfo = request.LoginUserInfo;
        LoginUser response = new LoginUser();
        Random random = new Random();
        SaLoginUser saLoginUser = new SaLoginUser();
        LoginUserInfo userInfo = new LoginUserInfo();

        Employee ManagerInfo = null;

        ManagerInfo managerInfo = new ManagerInfo();
        if (emp != null && emp.PARENT_ID != null && emp.PARENT_ID > 0) {
            ManagerInfo = employeeEntityService.findOne(emp.PARENT_ID);
            if (ManagerInfo != null) {
                managerInfo.Emp_ID = String.valueOf(ManagerInfo.Emp_ID);
                managerInfo.Email = ManagerInfo.Email;
                managerInfo.Name = ManagerInfo.Name;
                managerInfo.User_Name = ManagerInfo.User_Name;
                managerInfo.phoneNumber = ManagerInfo.Mobile_Phone;

            }
        }

        response.employeeId = emp.Emp_ID.toString();
        LoginUserInfo.userID = emp.Emp_ID.toString();
        response.managerInfo = managerInfo;
        response.isSuperAdmin = emp.getIsSuperAdmin();

        //validate groups to get active only
        emp.userGroups = validateActiveUserGroups(emp.userGroups);

        if ((emp.userGroups == null || emp.userGroups.size() <= 0) && emp.getUserType().equals(CommonConstants.USER_TYPE_NORMAL)) {
            throw new BusinessException(CodesAndKeys.USER_NOT_ASSIGNED_GROUP_CODE, CodesAndKeys.USER_NOT_ASSIGNED_GROUP_KEY, CodesAndKeys.USER_NOT_ASSIGNED_GROUP_MESSAGE);
        } else {
            response.UserGroups = new ArrayList<UserGroup>();
            if (emp.userGroups != null) {
                for (Group group : emp.userGroups) {
                    if (group.isHaveAdminPrev != null && group.isHaveAdminPrev)
                        response.isHaveAdminPrev = true;
                    UserGroup userGroup = new UserGroup();
                    userGroup.Group_Name = group.Name;
                    userGroup.GroupID = group.Group_ID;
                    userGroup.isdefault = (group.isDefault == null) ? false : group.isDefault;
                    userGroup.UserID = emp.Emp_ID;
                    response.UserGroups.add(userGroup);

                }
            }
        }

        userInfo.EmpStatus = emp.Status_ID;
        userInfo.StartWorkingHour = emp.startWorkingHour;
        userInfo.WorkingHours = emp.workinghours;
        userInfo.WorkingDays = emp.WorkingDays;
        userInfo.expiredate = emp.Expire_Date;
        userInfo.fullName = emp.Name;
        userInfo.hourcost = emp.HourCost;
        userInfo.Image = emp.image;
        userInfo.Mail = emp.Email;
        userInfo.BranchName = emp.locationName;
        userInfo.userType = emp.getUserType();
        long sessionIdRandom = random.nextLong();
        LoginUserInfo.sessionID = System.currentTimeMillis() + "-" + emp.Emp_ID + "-" + sessionIdRandom;
        userInfo.SessionInfo = LoginUserInfo;
        response.MainLoginInfo = userInfo;
        response.MainLoginInfo.isHaveAdminPrev = response.isHaveAdminPrev;
        saLoginUser.companyId = LoginUserInfo.companyId;
        saLoginUser.companyName = LoginUserInfo.companyName;
        saLoginUser.loginUserName = LoginUserInfo.loginUserName;
        saLoginUser.sessionID = LoginUserInfo.sessionID;
        saLoginUser.transactionID = LoginUserInfo.transactionID;
        saLoginUser.userID = LoginUserInfo.userID;
        saLoginUser.loginDate = new Date();
        saLoginUser.loginTime = new Time(saLoginUser.loginDate.getTime());
        saLoginUser.userIp = request.userIp;
        saLoginUser.userAgent = request.userAgent;
        saLoginUser.userPlatform = request.userPlatform;
        saLoginUserRepo.save(saLoginUser);
        logger.debug("end buildLoginResponse function");
        return response;
    }


    private List<Group> validateActiveUserGroups(List<Group> userGroups) {

        List<Group> acvtiveGroups = null;

        if (Utils.isNotEmpty(userGroups)) {
            acvtiveGroups = userGroups.stream().filter(grp -> grp.Status_ID.equals(CommonConstants.GROUP_ACTIVATED))
                    .collect(Collectors.toList());
        }

        return acvtiveGroups;
    }


    public Boolean getFailedTrialsExceeded(String userName) {

        Long maxLoginAttempts = Long.parseLong(configurationEntityService.getByKey(ConfigurationConstant.MAX_FAILED_LOGIN));
        Employee employee = employeeEntityService.findByUsername(userName);
        return (employee != null && employee.getFailedLoginCounter() != null && employee.getFailedLoginCounter() >= maxLoginAttempts - 1);

    }

    public ResponseEntity<?> checkOldPasswordBeforeReset(LoginUserRequest req) {
        // set company from request
        TenantContext.setCompanyName(req.companyName);

        Employee employee = employeeEntityService.findOne(Long.parseLong(req.LoginUserInfo.userID));
        //Check Employee Login_Type and then Check Password and Login
        if (employee.loginType == CommonConstants.LOGIN_TYPE_DB) {
            if (!passwordEncoder.matches(req.Password, employee.Password)) {
                throw new BusinessException(CodesAndKeys.INVALID_USERNAME_PASSWORD_CODE, CodesAndKeys.INVALID_USERNAME_PASSWORD_KEY, CodesAndKeys.INVALID_USERNAME_PASSWORD_MESSAGE);
            }
        }
        return null;

    }


    public Employee findByUsername(@PathVariable String username) {
        return employeeEntityService.findByUsername(username);
    }

    //Dev-00002380: LDAP Data while creating user @Ekhaled
    public Employee getLdapData(HashMap<String,String> targetUser){

        String ldapDomain = configurationEntityService.getByKey(ConfigurationConstant.LDAP_DOMAIN);
        String ldapUrl = configurationEntityService.getByKey(ConfigurationConstant.LDAP_URL);
        String ldapPassword = configurationEntityService.getByKey(ConfigurationConstant.SERVER_PASSWORD);
        String baseDN = configurationEntityService.getByKey(ConfigurationConstant.BASE_DN);
        String ldapUsername = configurationEntityService.getByKey(ConfigurationConstant.SERVER_ADMIN);

        return ldapUtils.getLdapData(ldapDomain,ldapUrl,ldapUsername,ldapPassword,baseDN, targetUser.get("username"));

    }

    /**
     * @param req
     * @return
     */

    public LoginUserResponse refreshLoginUserInfo(LoginUserRequest req) {
        LoginUser response = new LoginUser();
        try {

            // Dev-00003536:logging with Demo link mobile requirments
            if(req.companyName!=null) {
                TenantContext.setCompanyName(req.companyName);
            }
            else{
                TenantContext.setCompanyName(req.LoginUserInfo.companyName);
            }

            LoginUserInfo userInfo = new LoginUserInfo();
            SessionInfo loginUserInfo = req.LoginUserInfo;

            SaLoginUser saLoginUser = saLoginUserRepo.findBySessionID(loginUserInfo.sessionID);
            if (saLoginUser == null) {
                throw new BusinessException(CodesAndKeys.SESSION_EXPIRED_CODE, CodesAndKeys.SESSION_EXPIRED_KEY, CodesAndKeys.SESSION_EXPIRED_MESSAGE);
            }
            if (!saLoginUser.userID.equals(req.LoginUserInfo.userID)) {
                throw new BusinessException(CodesAndKeys.INVALID_SESSION_ID_CODE, CodesAndKeys.INVALID_SESSION_ID_KEY, CodesAndKeys.INVALID_SESSION_ID_MESSAGE);
            }
            Employee emp = employeeEntityService.findEmployeeById(Long.parseLong(saLoginUser.userID));
            if (emp == null) {
                throw new BusinessException(CodesAndKeys.INVALID_SESSION_ID_CODE, CodesAndKeys.INVALID_SESSION_ID_KEY, CodesAndKeys.INVALID_SESSION_ID_MESSAGE);
            }
            //username is case-insensitive
            if (!saLoginUser.loginUserName.equalsIgnoreCase(emp.User_Name)
                    || !saLoginUser.userID.equals(emp.Emp_ID.toString())) {
                throw new BusinessException(CodesAndKeys.SESSION_EXPIRED_CODE, CodesAndKeys.SESSION_EXPIRED_KEY, CodesAndKeys.SESSION_EXPIRED_MESSAGE);
            }
            response.employeeId = emp.Emp_ID.toString();
            loginUserInfo.loginUserName = emp.User_Name;

            if (emp.userGroups.size() <= 0) {
                throw new NTGRestException("", "this user is not in any group");
            } else {
                response.UserGroups = new ArrayList<UserGroup>();
                for (Group group : emp.userGroups) {
                    if (group.isHaveAdminPrev = true)
                        response.isHaveAdminPrev = true;

                    UserGroup userGroup = new UserGroup();
                    userGroup.Group_Name = group.Name;
                    userGroup.GroupID = group.Group_ID;
                    userGroup.isdefault = (group.isDefault == null) ? false : group.isDefault;
                    userGroup.UserID = emp.Emp_ID;
                    response.UserGroups.add(userGroup);

                }
            }
            userInfo.EmpStatus = emp.Status_ID;

            userInfo.StartWorkingHour = emp.startWorkingHour;
            userInfo.WorkingHours = emp.workinghours;
            userInfo.expiredate = emp.Expire_Date;
            userInfo.fullName = emp.Name;
            userInfo.hourcost = emp.HourCost;
            userInfo.Image = emp.image;
            userInfo.Mail = emp.Email;
            userInfo.BranchName = emp.locationName;
            userInfo.WorkingDays = emp.WorkingDays;
            userInfo.SessionInfo = loginUserInfo;
            response.MainLoginInfo = userInfo;

            LoginUserResponse res = new LoginUserResponse();
            res.loginUser = response;
            return res;

        } catch (NTGRestException e) {
            NTGMessageOperation.PrintErrorTrace(e);
            response.restException = e;
            LoginUserResponse res = new LoginUserResponse();
            res.loginUser = response;
            return res;
        }

    }

}
