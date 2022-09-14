--begin;
DO $$DECLARE r record;
BEGIN
FOR r IN(select table_name, column_name from information_schema.columns where lower(table_name)='comp_employee' and  lower(data_type) like '%timestamp without time zone%'
)
        LOOP
             EXECUTE 'ALTER TABLE ' || r.table_name || ' ALTER COLUMN ' || r.column_name || ' TYPE TIMESTAMP WITH TIME ZONE';
END LOOP;
    END$$;
--end;
