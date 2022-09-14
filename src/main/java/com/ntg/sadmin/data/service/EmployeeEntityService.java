package com.ntg.sadmin.data.service;

import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.constants.CommonConstants;
import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeEntityService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee findByUsername(String User_Name) {
        return employeeRepository.findByUsername(User_Name, TenantContext.getCompanyName());
    }

    public Employee findByMail(String mail) {
        return employeeRepository.findByMail(mail, TenantContext.getCompanyName());
    }

    public Employee findByUsernameAndId(String User_Name, Long Id) {
        return employeeRepository.findByUsernameAndId(User_Name, Id, TenantContext.getCompanyName());
    }

    public Employee findByMailANDId(String mail, Long Id) {
        return employeeRepository.findByMailANDId(mail, Id, TenantContext.getCompanyName());
    }

    public Iterable<Employee> findAll() {
        return employeeRepository.findAll(TenantContext.getCompanyName());
    }

    public Employee findOne(long empId) {
        return employeeRepository.findById(empId).orElse(null);
    }

    public Employee save(Employee emp) {
        emp = employeeRepository.save(emp);
        return emp;
    }

    public void handleEmployeeFailedLogin(Employee employee, boolean incrementFailedLogin, boolean incrementTemporaryLock, Long statusId) {
        if (incrementFailedLogin) {
            employee.setFailedLoginCounter((employee.getFailedLoginCounter() + 1));//Increment Failed Login Counter
        }
        if (incrementTemporaryLock) {
            employee.setTemporaryLockCounter(employee.getTemporaryLockCounter() + 1);
            employee.setFailedLoginCounter(0L);
        }
        if (statusId != null) {
            employee.Status_ID = statusId;
            if (statusId.equals(CommonConstants.ACCOUNT_TEMPORARY_LOCKED)) {
                Date timeNow = new Date();
                employee.setAccountTemporaryLockedStartTime(timeNow);
                employee.statusName = CommonConstants.ACCOUNT_TEMPORARY_LOCKED_STRING;
            } else if (statusId.equals(CommonConstants.ACCOUNT_SUSPENDED)) {
                employee.setTemporaryLockCounter(0L);
                employee.statusName = CommonConstants.ACCOUNT_SUSPENDED_STRING;
            } else if (statusId.equals(CommonConstants.ACCOUNT_ACTIVATED)) {
                employee.setTemporaryLockCounter(0L);
                employee.statusName = CommonConstants.ACCOUNT_ACTIVATED_STRING;
            }
        }
        employeeRepository.save(employee);
    }

    public void handleEmployeeFailedLoginForRecaptcha(Employee employee) {
        employee.setFailedLoginCounter((employee.getFailedLoginCounter() + 1));//Increment Failed Login Counter
        employeeRepository.save(employee);
    }

    public void updatePassword(String password , Long recId , String companyName){
        employeeRepository.updatePassword(recId , password , companyName);
    }


    public Iterable<Employee> findByPasswordNotLike(String password) {
        return employeeRepository.findByPasswordNotLike(password, TenantContext.getCompanyName());
    }

    public Long GetEmployeeCountsInDB() {
        return employeeRepository.getEmployeesCount(TenantContext.getCompanyName());
    }

    public void updateEmployeeParentId(List<Long> employees_id, Long managerId) {
        employeeRepository.updateEmployeeParentId(employees_id, managerId, TenantContext.getCompanyName());
    }

    public Employee findByUsernameAndIsSuperAdmin(String User_Name) {
        return employeeRepository.findByUsernameAndIsSuperAdmin(User_Name);
    }

    public List<Employee> findAllChildrenOfEmployee(long managerId) {
        return employeeRepository.findAllChildrenOfEmployee(managerId, TenantContext.getCompanyName());
    }

    public List<Employee> findByUserNameAndEmailAndName(String User_Name, String name, String email) {
        return employeeRepository.findByUserNameAndEmailAndName(User_Name, name, email, TenantContext.getCompanyName());
    }

    public List<Employee> findEmployeeTwoFactorAuthCode(String user_name, String twoFactorCode, long expireTime) {
        return employeeRepository.findEmployeeTwoFactorAuthCode(user_name, twoFactorCode, expireTime, TenantContext.getCompanyName());
    }

    public String findMobileFactorAuthCode(String user_name) {
        return employeeRepository.getMobileAndMobile(user_name, TenantContext.getCompanyName());
    }

    public void updateFactorAuthCode(String user_name, String Code, long exp, long expnow) {
        employeeRepository.updateCodeAuth(Code, exp, user_name, expnow, TenantContext.getCompanyName());
    }

    public List<Employee> findEmployeesByIds(List<Long> employeesIds) {
        return employeeRepository.findEmployeesByIds(employeesIds, TenantContext.getCompanyName());
    }

    public Employee findEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeById(employeeId);
    }

    public List<Employee> findEmployeeByCompanyName(String companyName) {
        return employeeRepository.findByCompanyName(companyName);
    }

    public List<Employee> findEmployeeByCompanyNameAndUserName(String companyName,String User_Name) {
        return employeeRepository.findByCompanyNameAAndUser_Name(companyName,User_Name);
    }
}
