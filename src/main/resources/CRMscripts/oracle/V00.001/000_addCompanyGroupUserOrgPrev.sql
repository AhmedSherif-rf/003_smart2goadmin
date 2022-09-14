INSERT INTO SA_GROUP (RECID, CREATED_BY_COMPANY, CREATED_BY_NAME, DESCRIPTION, EMAIL, GRUOP_TYPE, ISDEFAULT, NAME, STATUSID) VALUES ('1', 'admin', 'admin', 'NTG', 'yghandor@ntgclarity.com', '1', '1', 'NTG', '1');

INSERT INTO SA_GROUP (RECID, CREATED_BY_COMPANY, CREATED_BY_NAME, DESCRIPTION, PARENT_GROUP_ID, EMAIL, GRUOP_TYPE, ISDEFAULT, NAME, STATUSID) VALUES ('2', 'NTG', 'admin', 'organization', '1', 'yghandor@ntgclaruty.com', '2', '1', 'organization', '1');

INSERT INTO SA_GROUP (RECID, CREATED_BY_COMPANY, CREATED_BY_NAME, DESCRIPTION, EMAIL, GRUOP_TYPE, NAME, STATUSID ,ISDEFAULT,PARENT_GROUP_ID , is_have_admin_prev) VALUES ('3', 'NTG', 'admin', 'the admin group', 'yghandor@ntgclarity.com', '3', 'Admin Group', '1','1' , '2' ,'1');

INSERT INTO SA_GROUP (RECID, CREATED_BY_COMPANY, CREATED_BY_NAME, DESCRIPTION, EMAIL, GRUOP_TYPE, NAME, STATUSID ,ISDEFAULT,PARENT_GROUP_ID , is_have_admin_prev) VALUES ('4', 'NTG', 'admin', 'Ldap new users', 'yghandor@ntgclarity.com', '3', 'Ldap users', '1','0' , '2' ,'0');





    INSERT INTO comp_employee (
        recid,
        contract_typeid,
        email,
        hourcost,
        mobile_phone,
        name,
        first_name,
        last_name,
        password,
        prefered_language,
        statusid,
        user_name,
        workingdays,
        gender,
        location_name,
        status_name,
        corporate_id,
        deptid,
        parent_id,
        login_type,
        workinghours,
        startworkinghour
    ) VALUES (
        '1',
        '1',
        'yghandor@ntgclarity.com',
        '100',
        '01012135040',
        'Admin Admin',
        'Admin',
        'Admin',
        '   ',
        'Eng',
        '1',
        'admin',
        '1,2,3,4,5',
        'male',
        'Egypt',
        'active',
        '0',
        '0',
        '0',
        '0',
        '9',
        TO_DATE('1970-01-02 08:30:49', 'YYYY-MM-DD HH24:MI:SS')
    );
    
     

INSERT INTO SA_USER_GROUP (GROUPID, USERID) VALUES ('3', '1');

-- INSERT INTO ADMIN_GROUP_PREV (GROUP_ID, ASSIGNED_GROUP_PERMISSION, AUTHORIZED_TO_CLOSETT, CLEAR_SITE_DATA, DEFAULT_GROUP_PERMISSION, EDIT_CHRONIC, GLOBAL_ALLOWED, GLOBAL_INCEDENTS, GROUP_DESCRIPTION, GROUP_NAME, GROUP_STATUS, LOGICAL_GROUP, MANAGED_SERVICE, NFM_GROUP, PERFORMANCE_DESK, REASSIGNMENT_PREVENT, ROOT_CAUSE_REQUIRED, SA_ALLOWED, SOC_TEAMS, TECH_IM, TEU_ASSIGNEE, TT_CLOSURE, VOICE_IM) VALUES ('1', 'Comm Approval', '1', '1', 'Finance', '1', '1', '1', 'Admin', 'Admin Group', 'Active', '0', '1', '0', '1', '1', '0', '1', '1', '1', '1', '0', '1');


