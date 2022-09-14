--begin;
DO $$DECLARE r record;
BEGIN
FOR r IN(SELECT
    groupid,
	userid,
    COUNT( userid ),
	min(recid) as recid
FROM
    sa_user_group_new
GROUP BY
    groupid, userid
HAVING
    COUNT( userid )> 1
ORDER BY
    groupid)
        LOOP
             EXECUTE 'delete from sa_user_group_new where RECID = ' || r.recid || ';';
END LOOP;
    END$$;
--end;