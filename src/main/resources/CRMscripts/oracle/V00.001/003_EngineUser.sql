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
        startworkinghour,
        user_type
    ) VALUES (
        '2',
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
        'engine',
        '1,2,3,4,5',
        'male',
        'Egypt',
        'active',
        '0',
        '0',
        '0',
        '0',
        '9',
        TO_DATE('1970-01-02 08:30:49', 'YYYY-MM-DD HH24:MI:SS'),
        '1'
    );
