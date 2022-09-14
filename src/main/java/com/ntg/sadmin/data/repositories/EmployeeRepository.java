package com.ntg.sadmin.data.repositories;

import com.ntg.sadmin.data.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Transactional
    @Query(value = "SELECT * FROM comp_employee  WHERE lower(user_name)  = lower(:User_Name) and company_name = :companyName", nativeQuery = true)
    Employee findByUsername(@Param("User_Name") String User_Name, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee WHERE lower(email)  = lower(:mail) and company_name = :companyName", nativeQuery = true)
    Employee findByMail(@Param("mail") String mail, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee WHERE password Not Like :password and company_name = :companyName", nativeQuery = true)
    Iterable<Employee> findByPasswordNotLike(@Param("password") String password, @Param("companyName") String companyName);

    @Modifying
    @Transactional
    @Query(value = "update comp_employee set PARENT_ID = :managerId where recid in (:Employees)  and company_name = :companyName", nativeQuery = true)
    void updateEmployeeParentId(@Param("Employees") List<Long> Employees, @Param("managerId") Long managerId, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "select count(1) from comp_employee where company_name = :companyName", nativeQuery = true)
    Long getEmployeesCount(@Param("companyName") String companyName);

    @Transactional
    @Query(value = "select * from comp_employee where company_name = :companyName", nativeQuery = true)
    List<Employee> findAll(@Param("companyName") String companyName);

    @Transactional
    @Modifying
    @Query(value = "delete from comp_employee where company_name = :companyName", nativeQuery = true)
    void deleteAll(@Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee  WHERE lower(user_name)  = lower(:User_Name) and is_super_admin = '1'", nativeQuery = true)
    Employee findByUsernameAndIsSuperAdmin(@Param("User_Name") String User_Name);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee  WHERE lower(user_name) = lower(:User_Name) and recId !=:recId and company_name = :companyName", nativeQuery = true)
    Employee findByUsernameAndId(@Param("User_Name") String user_name, @Param("recId") Long recId, @Param("companyName") String companyName);

    @Query(value = "SELECT * FROM comp_employee  WHERE lower(email)  = lower(:mail) and recId !=:recId and user_type != 1 and company_name = :companyName", nativeQuery = true)
    Employee findByMailANDId(@Param("mail") String mail, @Param("recId") Long recId, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee  WHERE  PARENT_ID = :managerId and user_type != 1 and company_name = :companyName", nativeQuery = true)
    List<Employee> findAllChildrenOfEmployee(@Param("managerId") long managerId, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee c WHERE  lower(c.user_name) LIKE %:User_Name% and lower(c.name) LIKE %:name% and lower(c.email) LIKE %:email% and c.company_name = :companyName", nativeQuery = true)
    List<Employee> findByUserNameAndEmailAndName(@Param("User_Name") String User_Name, @Param("name") String name, @Param("email") String email, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee c WHERE  c.is_2fa_code= :twoFactorCode and c.user_name= :user_name and c.is_expire_time >= :expireTime and c.company_name = :companyName", nativeQuery = true)
    List<Employee> findEmployeeTwoFactorAuthCode(@Param("user_name") String user_name, @Param("twoFactorCode") String twoFactorCode, @Param("expireTime") long expireTime, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT mobile_phone FROM comp_employee c WHERE c.user_name= :user_name  and c.company_name = :companyName", nativeQuery = true)
    String getMobileAndMobile(@Param("user_name") String user_name, @Param("companyName") String companyName);

    @Modifying
    @Transactional
    @Query(value = "update comp_employee set  is_2fa_code = :twoFactorCode, is_expire_time = :expireTime where user_name = :user_name  and  is_expire_time < :expireTimeNow  and company_name = :companyName", nativeQuery = true)
    void updateCodeAuth(@Param("twoFactorCode") String twoFactorCode, @Param("expireTime") long expireTime, @Param("user_name") String user_name, @Param("expireTimeNow") long expireTimeNow, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "select * from comp_employee where recid in(:employeeIds) and company_name = :companyName", nativeQuery = true)
    List<Employee> findEmployeesByIds(@Param("employeeIds") List<Long> employeeIds, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "select * from comp_employee where recid = :employeeId", nativeQuery = true)
    Employee findEmployeeById(@Param("employeeId") Long employeeId);

    @Modifying
    @Transactional
    @Query(value = "update comp_employee set  password = :password where recId in (:recId)  and company_name = :companyName", nativeQuery = true)
    void updatePassword(@Param("recId") Long recId , @Param("password") String password, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT * FROM comp_employee WHERE  company_name = :companyName", nativeQuery = true)
    List<Employee> findByCompanyName(@Param("companyName") String companyName);


    @Transactional
    @Query(value = "SELECT * FROM comp_employee WHERE  company_name = :companyName And User_name = :user_name ", nativeQuery = true)
    List<Employee> findByCompanyNameAAndUser_Name(@Param("companyName") String companyName,@Param("user_name") String user_name);

}
