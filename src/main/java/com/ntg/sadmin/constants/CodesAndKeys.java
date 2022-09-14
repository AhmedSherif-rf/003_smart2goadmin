package com.ntg.sadmin.constants;

/**
 * CodesAndKeys Will be used in for codes returned in any request
 *
 * @author mashour@ntgclarity.com
 */
public interface CodesAndKeys {

    String INTERNAL_SERVER_ERROR_CODE = "500";
    String INTERNAL_SERVER_ERROR_KEY = "INTERNAL_SERVER_ERROR";
    String INTERNAL_SERVER_ERROR_MESSAGE = "Your request has failed";

    String BUSINESS_ERROR_CODE = "400";
    String BUSINESS_ERROR_KEY = "BUSINESS_ERROR";
    String BUSINESS_ERROR_MESSAGE = "Your request has failed";

    String SUCCESS_CODE = "200";
    String SUCCESS_KEY = "SUCCESS";
    String SUCCESS_MESSAGE = "Your request has done successfully";

    String INVALID_USERNAME_PASSWORD_CODE = "001";
    String INVALID_USERNAME_PASSWORD_KEY = "INVALID_USERNAME_PASSWORD";
    String INVALID_USERNAME_PASSWORD_MESSAGE = "Invalid username and password";

    String USER_NOT_ASSIGNED_GROUP_CODE = "002";
    String USER_NOT_ASSIGNED_GROUP_KEY = "USER_NOT_ASSIGNED_GROUP";
    String USER_NOT_ASSIGNED_GROUP_MESSAGE = "This user not assigned to any group";

    String INVALID_PASSWORD_STRENGTH_CODE = "003";
    String INVALID_PASSWORD_STRENGTH_KEY = "INVALID_PASSWORD_STRENGTH";
    String INVALID_PASSWORD_STRENGTH_MESSAGE = "Invalid password strength";

    String ACCOUNT_TEMPORARY_LOCKED_CODE = "004";
    String ACCOUNT_TEMPORARY_LOCKED_KEY = "ACCOUNT_TEMPORARY_LOCKED";
    String ACCOUNT_TEMPORARY_LOCKED_MESSAGE = "Dear User, Your Account is Temporarily Locked and will be Automatically Re-Activated";

    String ACCOUNT_SUSPENDED_CODE = "005";
    String ACCOUNT_SUSPENDED_KEY = "ACCOUNT_SUSPENDED";
    String ACCOUNT_SUSPENDED_MESSAGE = "Dear User, Your Account is De-Activated, Contact DB Admin to Activate your Account";

    String ACCOUNT_TERMINATED_CODE = "006";
    String ACCOUNT_TERMINATED_KEY = "ACCOUNT_TERMINATED";
    String ACCOUNT_TERMINATED_MESSAGE = "Dear User, Your Account has been Terminated";

    String SESSION_EXPIRED_CODE = "007";
    String SESSION_EXPIRED_KEY = "SESSION_EXPIRED";
    String SESSION_EXPIRED_MESSAGE = "Session expired";

    String INVALID_SESSION_ID_CODE = "008";
    String INVALID_SESSION_ID_KEY = "INVALID_SESSION_ID";
    String INVALID_SESSION_ID_MESSAGE = "Invalid session id";

    String NUMBER_OF_FAILED_LOGINS_EXCEEDED_CODE = "009";
    String NUMBER_OF_FAILED_LOGINS_EXCEEDED_KEY = "NUMBER_OF_FAILED_LOGINS_EXCEEDED";
    String NUMBER_OF_FAILED_LOGINS_EXCEEDED_MESSAGE = "Number of failed logins exceeded";
    
    String PASSWORD_EXPIRED_CODE = "010";
    String PASSWORD_EXPIRED_KEY = "PASSWORD_EXPIRED";
    String PASSWORD_EXPIRED_MESSAGE = "Dear User, Your Password has been expired, Please Reset your password";

    String WRONG_LDAP_CONFIGURATION_CODE = "011";
    String WRONG_LDAP_CONFIGURATION_KEY = "WRONG_LDAP_CONFIGURATION";
    String WRONG_LDAP_CONFIGURATION_MESSAGE = "LDAP Settings not configured well, please contact System Administrator";

    String USER_NOT_FOUND_CODE = "012";
    String USER_NOT_FOUND_KEY = "USER_NOT_FOUND";
    String USER_NOT_FOUND_MESSAGE = "This user doesn't exist, please contact System Administrator";
}
