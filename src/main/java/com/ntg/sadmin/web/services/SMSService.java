package com.ntg.sadmin.web.services;


import com.ntg.sadmin.data.service.EmployeeEntityService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SMSService {
    @Autowired
    private EmployeeEntityService employeeEntityService;
    private final static String ACCOUNT_SID = "AC96a3eb8285f4960f9185105aa5f0c639";
    private final static String  AUTH_TOKEN = "567f1ece783099a5da34d36937824fe6";


    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public boolean send2FaCode(String user_name, String twoFaCode,String NumberEnterCode) {
        System.out.println("start sending messages");
        long OTP_VALID_DURATION =  60 * 1000;   // 5 minutes
        long time = System.currentTimeMillis()/1000;
        Boolean isValidNumber = false;
        String mobilenumber=employeeEntityService.findMobileFactorAuthCode(user_name);
         if(mobilenumber !=null) {
                 Message.creator(new PhoneNumber("+20" + mobilenumber), new PhoneNumber("+17197452952"),
                         "Your Two Factor Authentication code is: " + twoFaCode).create();
                 System.out.println("message send successfully");
               isValidNumber = true;
         }else{
             isValidNumber = false;
         }
        return  isValidNumber;
    }

}
