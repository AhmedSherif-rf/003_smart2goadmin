CREATE SEQUENCE sa_user_group_s
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
	
Alter table sa_user_group add column RECID numeric primary key;	
create table sa_user_group_new as (select NEXTVAL('sa_user_group_s') as recid, groupid, userid from sa_user_group);