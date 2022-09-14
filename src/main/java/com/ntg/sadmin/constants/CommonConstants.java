package com.ntg.sadmin.constants;

public interface CommonConstants {

    String INTEGRATION_JSON_VIRTUAL_ROOT = "{\"VIRTUAL_ROOT\":";

    // DEFAULT_LOGIN_TYPE
    int LOGIN_TYPE_DB = 0;
    int LOGIN_TYPE_LDAP = 1;
    int LOGIN_TYPE_SMTP = 2;

    String APPLICATION_PROPERTIES_FILE_NAME = "application_sadmin.properties";

    // USERS_TYPE_IN_SYSTEM
    Long USER_TYPE_NORMAL = 0L;
    Long USER_TYPE_SYSTEM = 1L;

    // User Status ID
    Long ACCOUNT_ACTIVATED = 1L;
    Long ACCOUNT_SUSPENDED = 2L;
    Long ACCOUNT_TERMINATED = 3L;
    Long ACCOUNT_TEMPORARY_LOCKED = 4L;

    // User Status String
    String ACCOUNT_ACTIVATED_STRING = "Active";
    String ACCOUNT_SUSPENDED_STRING = "Suspended";
    String ACCOUNT_TERMINATED_STRING = "Terminated";
    String ACCOUNT_TEMPORARY_LOCKED_STRING = "Locked";

    // Group Status ID
    Long GROUP_ACTIVATED = 1L;
    Long GROUP_SUSPENDED = 2L;
    Long GROUP_TERMINATED = 3L;

}
