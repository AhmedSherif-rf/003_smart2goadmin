UPDATE comp_employee  SET User_name = substring(User_name ,1, position('@' IN User_name)-1)
where  User_Name like '%@%';
