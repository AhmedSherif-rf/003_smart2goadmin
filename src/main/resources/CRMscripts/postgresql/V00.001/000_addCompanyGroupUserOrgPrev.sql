INSERT INTO SA_GROUP (RECID, NAME, GRUOP_TYPE, ISDEFAULT, STATUSID) VALUES ('1', 'Company', '1', '1', '1');

INSERT INTO SA_GROUP (RECID, NAME, PARENT_GROUP_ID, GRUOP_TYPE, ISDEFAULT, STATUSID) VALUES ('2', 'Organization', '1', '2', '1', '1');

INSERT INTO SA_GROUP (RECID, NAME, GRUOP_TYPE, STATUSID ,ISDEFAULT,PARENT_GROUP_ID , is_have_admin_prev) VALUES ('3', 'Admin Group', '3','1' ,'1' ,'2' ,'1');

INSERT INTO SA_GROUP (RECID, NAME, GRUOP_TYPE, STATUSID ,ISDEFAULT,PARENT_GROUP_ID , is_have_admin_prev) VALUES ('4', 'Ldap users', '3', '1','0' , '2' ,'0');






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
        'NTSAdmin@ntgclarity.com',
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
        '02:03:04'
    );
    
     

INSERT INTO SA_USER_GROUP (GROUPID, USERID) VALUES ('3', '1');

-- INSERT INTO ADMIN_GROUP_PREV (GROUP_ID, ASSIGNED_GROUP_PERMISSION, AUTHORIZED_TO_CLOSETT, CLEAR_SITE_DATA, DEFAULT_GROUP_PERMISSION, EDIT_CHRONIC, GLOBAL_ALLOWED, GLOBAL_INCEDENTS, GROUP_DESCRIPTION, GROUP_NAME, GROUP_STATUS, LOGICAL_GROUP, MANAGED_SERVICE, NFM_GROUP, PERFORMANCE_DESK, REASSIGNMENT_PREVENT, ROOT_CAUSE_REQUIRED, SA_ALLOWED, SOC_TEAMS, TECH_IM, TEU_ASSIGNEE, TT_CLOSURE, VOICE_IM) VALUES ('1', 'Comm Approval', '1', '1', 'Finance', '1', '1', '1', 'Admin', 'Admin Group', 'Active', '0', '1', '0', '1', '1', '0', '1', '1', '1', '1', '0', '1');


