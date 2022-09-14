package com.ntg.sadmin.web.controllers;

import com.ntg.sadmin.constants.CommonConstants;
import com.ntg.sadmin.constants.ConfigurationConstant;
import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.service.ConfigurationEntityService;
import com.ntg.sadmin.data.service.EmployeeEntityService;
import com.ntg.sadmin.web.response.StringResponse;
import com.ntg.sadmin.web.services.DAOService;
import com.ntg.sadmin.web.services.SMSService;
import com.ntg.sadmin.web.services.SmppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@RestController
public class TwoFactorServiceController {

    String NumberEnterCode ="available";

    @Autowired
    DAOService daoService;
    @Autowired
    SMSService smsService;
    @Autowired
    SmppService smppsms;
    @Autowired
    private EmployeeEntityService employeeEntityService;
    @Autowired
    private ConfigurationEntityService configurationEntityService;

    @RequestMapping(value="/EmployeeMobilenumber2fa", method= RequestMethod.POST)
    public ResponseEntity<Object> send2faCodeinSMS(@RequestBody() HashMap<String, String> authObj ) {
        String twoFaCode = String.valueOf(new Random().nextInt(9999) + 1000);
        //  boolean IsnumberFound = smsService.send2FaCode(authObj.get("user_name").toString(), twoFaCode,NumberEnterCode);
        HashMap<String,String> SendValid = smppsms.sendVerifyCode(authObj.get("user_name").toString(),
                                                          twoFaCode,
                                                          NumberEnterCode,
                                                          authObj.get("host").toString(),
                                                          authObj.get("port").toString(),
                                                          authObj.get("userName").toString(),
                                                          authObj.get("password").toString()
                                                          );


           if (SendValid.get("isValidNumber").equals("Yes")){
               if(SendValid.get("Sending").equals("Yes")) {
                   daoService.update2FAProperties(authObj.get("user_name").toString(), twoFaCode);
                   return new ResponseEntity<>(new StringResponse("Sending"), HttpStatus.OK);
               }
               return new ResponseEntity<>(new StringResponse("Found Number"), HttpStatus.OK);
           } else {
               return new ResponseEntity<>(new StringResponse("Not Found Number"), HttpStatus.OK);
           }

//        if (IsnumberFound){
//            daoService.update2FAProperties(authObj.get("user_name").toString(), twoFaCode);
//            return new ResponseEntity<>(new StringResponse("Found Number"), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(new StringResponse("Not Found Number"), HttpStatus.OK);
//        }
    }

    @RequestMapping(value="/Employeecode2fa", method=RequestMethod.POST)
    public ResponseEntity<?> verify(@RequestBody() HashMap<String, String> authObj ) {
        Employee employee = employeeEntityService.findByUsername(authObj.get("user_name").toString());
        if(employee.getSms_expire_code_Number() >= 3){
               Date timeNow = new Date();
               employee.setAccountTemporaryLockedStartTime(timeNow);
               employee.setStatus_ID(CommonConstants.ACCOUNT_TEMPORARY_LOCKED);
               employee.setStatusName(CommonConstants.ACCOUNT_TEMPORARY_LOCKED_STRING);
               employeeEntityService.save(employee);
        }
        boolean isValid = daoService.checkCode(authObj.get("user_name").toString(),authObj.get("code").toString());
        if(isValid) {
            employee.setSms_expire_code_Number(0l);
            employeeEntityService.save(employee);
            return new ResponseEntity<>(new StringResponse("valid"), HttpStatus.OK);
        } else{
            employee.setSms_expire_code_Number(employee.getSms_expire_code_Number() + 1);
            employeeEntityService.save(employee);
            return new ResponseEntity<>(new StringResponse("not valid"), HttpStatus.OK);
        }
    }
}

