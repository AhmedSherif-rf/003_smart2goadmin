package com.ntg.sadmin.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.ntg.sadmin.common.NTGMessageOperation;
import com.ntg.sadmin.constants.ConfigurationConstant;
import com.ntg.sadmin.data.entities.Employee;
import com.ntg.sadmin.data.service.ConfigurationEntityService;
import com.ntg.sadmin.data.service.EmployeeEntityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ntg.sadmin.constants.CodesAndKeys;
import com.ntg.sadmin.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LdapUtils {

    @Autowired
    private  ConfigurationEntityService configurationEntityService;

    @Autowired
    private EmployeeEntityService employeeRepo;


    public  boolean isLdapRegistered(String ldapDomain, String ldapUrl, String username, String password) {
         System.out.println("start isLdapRegistered function");
        boolean result = false;
        try {
            Hashtable env = new Hashtable();
            env = createEnvironmentVariablesForLdapServer(env, ldapDomain, ldapUrl, username, password);

            LdapContext ctx = new InitialLdapContext(env, null);
            result = ctx != null;
            if (ctx != null)
                ctx.close();
             System.out.println("end isLdapRegistered function");
            return result;
        } catch (Exception e) {
            System.err.println("end isLdapRegistered function with exception: " + e.getMessage());
            throw new BusinessException(CodesAndKeys.INVALID_USERNAME_PASSWORD_CODE, CodesAndKeys.INVALID_USERNAME_PASSWORD_KEY, CodesAndKeys.INVALID_USERNAME_PASSWORD_MESSAGE);
        }
    }

    public  void authenticatedMailSMTP(String host, String port, String userEmail, String userPassword, boolean isSupportSSL) {
        System.out.println("start authenticatedMailSMTP function");
        Transport transport = null;
        try {
            // mailSetting.authentication=NTLM



            String DEBUG = "true";
            // Use Properties object to set environment properties
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.starttls", "true");

            props.setProperty("mail.smtp.starttls.enable", "true");


            props.put("mailSetting.authentication", "NTLM");
            props.put("mail.debug", DEBUG);
            props.put("mail.smtp.auth", "true");
            if (isSupportSSL) {
                props.setProperty("mail.smtp.ssl.enable", "true");
            }else{
                props.setProperty("mail.smtp.ssl.enable", "false");
            }
            // Obtain the default mail session
            Session session = Session.getDefaultInstance(props, null);

            transport = session.getTransport();
            transport.connect(host, userEmail, userPassword);
            transport.close();
             System.out.println("end authenticatedMailSMTP function");
        } catch (Exception e) {
             System.err.println("end authenticatedMailSMTP function with exception: " + e.getMessage());
            throw new BusinessException(CodesAndKeys.INVALID_USERNAME_PASSWORD_CODE, CodesAndKeys.INVALID_USERNAME_PASSWORD_KEY, CodesAndKeys.INVALID_USERNAME_PASSWORD_MESSAGE);
        }
    }


    //Dev-00002380: LDAP Data while creating user @Ekhaled
    public  Employee getLdapData(String ldapDomain, String ldapUrl, String username, String password, String baseDN, String targetUser) {
         System.out.println("start getLdapData function");
        boolean result = false;
        try {

            Hashtable env = new Hashtable();
            env = createEnvironmentVariablesForLdapServer(env, ldapDomain, ldapUrl, username, password);

            DirContext myContext = new InitialDirContext(env);
            SearchControls searchCtrls = new SearchControls();
            searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String[] attributes = new String[10];

            attributes[0] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_USER_NAME);
            attributes[1] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_FIRST_NAME);
            attributes[2] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_LAST_NAME);
            attributes[3] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_EMAIL);
            attributes[4] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_LOCATION_NAME);
            attributes[5] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_GENDER);
            attributes[6] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_IMAGE);
            attributes[7] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_MOBILE);
            attributes[8] = configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_MANAGER);

            searchCtrls.setReturningAttributes(attributes);
            Employee ldapUserInfo;
            ldapUserInfo = searchUser(myContext, searchCtrls, targetUser, baseDN);


            if (myContext != null)
                myContext.close();
             System.out.println("end getLdapData function");
            return ldapUserInfo;
        } catch (Exception e) {
             System.err.println("end getLdapData function with exception: " + e.getMessage());
            NTGMessageOperation.PrintErrorTrace(e);
            throw new BusinessException(CodesAndKeys.WRONG_LDAP_CONFIGURATION_CODE, CodesAndKeys.WRONG_LDAP_CONFIGURATION_KEY, CodesAndKeys.WRONG_LDAP_CONFIGURATION_MESSAGE);
        }
    }

    private static Hashtable createEnvironmentVariablesForLdapServer(Hashtable env, String ldapDomain, String ldapUrl, String username, String password) {
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username + "@" + ldapDomain);
        env.put(Context.SECURITY_CREDENTIALS, password);

        return env;
    }

        private  Employee searchUser(DirContext myContext,SearchControls searchCtrls, String filterValue,String baseDN)throws NamingException {
        NamingEnumeration values = null;
        String filter_by_email = "mail";
        String filter_by_username = "sAMAccountName";
            Employee ldapUserInfo = new Employee();
        // filter target value by username or email
        if (filterValue.contains("@")) {
            values = myContext.search(baseDN, filter_by_email + "=" + filterValue,
                    searchCtrls);
        } else {
            values = myContext.search(baseDN, filter_by_username + "="
                    + filterValue, searchCtrls);
        }

        if (Utils.isNotEmpty(values)) {
            while (values.hasMoreElements()) {
                SearchResult result = (SearchResult) values.next();
                Attributes attribs = result.getAttributes();

                if (attribs != null) {
                    for (NamingEnumeration ae = attribs.getAll(); ae.hasMoreElements();) {
                        Attribute atr = (Attribute) ae.next();
                        String attributeID = atr.getID();
                        Enumeration vals = atr.getAll();
                        vals.hasMoreElements();

                         if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_FIRST_NAME))) {
                            String cn = (String) vals.nextElement();
                            int index = cn.indexOf(' ');
                            cn =(index > 0)? cn.substring(0, index) : cn;
                            ldapUserInfo.setName_First(cn);
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_LAST_NAME))) {
                            ldapUserInfo.setName_Last((String) vals
                                    .nextElement());
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_MOBILE))) {
                            ldapUserInfo.setMobile_Phone((String) vals.nextElement());
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_GENDER))) {
                            ldapUserInfo.setGender((String) vals.nextElement());
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_EMAIL))) {
                            ldapUserInfo.setEmail((String) vals.nextElement());
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_LOCATION_NAME))) {
                            ldapUserInfo.setLocationName((String) vals.nextElement());
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_USER_NAME))) {
                            String userPrincipalName = (String) vals.nextElement();
                            userPrincipalName = StringUtils.substringBefore(userPrincipalName,"@");
                            ldapUserInfo.setUser_Name(userPrincipalName);
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_IMAGE))) {
                            ldapUserInfo.setImage((byte[]) vals
                                    .nextElement());
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_IMAGE))) {
                            ldapUserInfo.setSmallImage((byte[]) vals
                                    .nextElement());
                        }
                        else if (attributeID.equalsIgnoreCase(configurationEntityService.getByKey(ConfigurationConstant.LDAP_SEARCH_CONTROL_FOR_MANAGER))) {
                             String manager = (String) vals.nextElement();
                             Employee emp = employeeRepo.findByUsername(manager);
                             if(Utils.isNotEmpty(emp)){
                                 ldapUserInfo.setPARENT_ID(emp.Emp_ID);
                             }
                         }
                    }

                    return ldapUserInfo;
                } else{
                    throw new BusinessException(CodesAndKeys.USER_NOT_FOUND_CODE, CodesAndKeys.USER_NOT_FOUND_KEY, CodesAndKeys.USER_NOT_FOUND_MESSAGE);
                }
            }
            throw new BusinessException(CodesAndKeys.USER_NOT_FOUND_CODE, CodesAndKeys.USER_NOT_FOUND_KEY, CodesAndKeys.USER_NOT_FOUND_MESSAGE);
        } else {
            throw new BusinessException(CodesAndKeys.USER_NOT_FOUND_CODE, CodesAndKeys.USER_NOT_FOUND_KEY, CodesAndKeys.USER_NOT_FOUND_MESSAGE);
        }

    }
}
