package com.ntg.sadmin.web.services;


import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.repositories.EmployeeRepository;
import com.ntg.sadmin.data.service.EmployeeEntityService;
import com.ntg.sadmin.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DAOService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private EmployeeEntityService employeeEntityService;

    public void update2FAProperties(String user_name, String twofacode) {
        //employeeEntityService.
        jdbcTemplate.update("update comp_employee set is_2fa_code=?, is_expire_time=? where user_name =?", new Object[] {
                twofacode, (System.currentTimeMillis()/1000) + 120, user_name
        });
    }


          public boolean checkCode(String user_name,String code) {
              long expNow=System.currentTimeMillis()/1000;
              Boolean isValidCode = false;
              List<Employee> employees = employeeEntityService.findEmployeeTwoFactorAuthCode(user_name, code, expNow);
              if(Utils.isNotEmpty(employees)) {
                  isValidCode =  true;
              } else {
                  employeeEntityService.updateFactorAuthCode(user_name, "", 0,expNow);
                  isValidCode = false;

              }
               return isValidCode;
    }



}
