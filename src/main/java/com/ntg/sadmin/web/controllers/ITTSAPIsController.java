package com.ntg.sadmin.web.controllers;

import com.ntg.sadmin.web.dto.LoginUser;
import com.ntg.sadmin.web.requests.GetToDoListRequest;
import com.ntg.sadmin.web.requests.LoginUserRequest;
import com.ntg.sadmin.web.requests.LogoutRequest;
import com.ntg.sadmin.web.requests.UserCompaniesListRequest;
import com.ntg.sadmin.web.services.ITTSAPIsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author abdelrahman
 */
@RestController()
@RequestMapping(value = "/ITTS_Main_WS/rest/BusIntel/APIs")
public class ITTSAPIsController {

    @Autowired
    private ITTSAPIsService ittsapIsService;

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/LogUser", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> LogUser(@RequestBody LoginUserRequest req) {

        if(req.enableRecaptcha){
            if(ittsapIsService.getFailedTrialsExceeded(req.LoginUserInfo.loginUserName)){
                LoginUser re = new LoginUser();
                re.employeeId = "reCAPTCHA Required";
                new ResponseEntity<>(re, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(ittsapIsService.LogUser(req), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/checkOldPasswordBeforeReset", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> checkOldPasswordBeforeReset(@RequestBody LoginUserRequest req) {

        return new ResponseEntity<>(ittsapIsService.checkOldPasswordBeforeReset(req), HttpStatus.OK);
    }

    @RequestMapping(value = "/FailedTrialsExceeded", method = RequestMethod.POST)
    public ResponseEntity<?> FailedTrialsExceeded(@RequestBody String userName) {
        return new ResponseEntity<>(ittsapIsService.getFailedTrialsExceeded(userName), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/CheckSessionValidityInfo", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> CheckSessionValidityInfo(@RequestBody LoginUserRequest req) throws Exception {
        return new ResponseEntity<>(ittsapIsService.CheckSessionValidityInfo(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/signOut", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> signOut(@RequestBody LogoutRequest req) {
        return new ResponseEntity<>(ittsapIsService.signOut(req), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetToDoList", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> GetToDoList(@RequestBody GetToDoListRequest req) {
        return new ResponseEntity<>(ittsapIsService.GetToDoList(req), HttpStatus.OK);
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/GetUserMessagesList", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> GetUserMessagesList() {
        return new ResponseEntity<>(ittsapIsService.GetUserMessagesList(), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getUserCompaniesList", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getUserCompaniesList(@RequestBody UserCompaniesListRequest req) {
        return new ResponseEntity<>(ittsapIsService.getUserCompaniesList(req), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/findByUsername/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        return new ResponseEntity<>(ittsapIsService.findByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/validateLdapUser", method = RequestMethod.POST)
    public ResponseEntity<?> validateLdapUser(@RequestBody HashMap<String,String> targetUser) throws Exception {
        return new ResponseEntity<>(ittsapIsService.getLdapData(targetUser), HttpStatus.OK);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refreshLoginUserInfo", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> refreshLoginUserInfo(@RequestBody LoginUserRequest req) throws Exception {
        return new ResponseEntity<>(ittsapIsService.refreshLoginUserInfo(req), HttpStatus.OK);
    }
}
