ALTER TABLE CONFIGURATION MODIFY (VALUE VARCHAR2(4000));
INSERT INTO CONFIGURATION (ID, KEY, VALUE,NOTE) VALUES (12, 'PASSWORD_REGEX', '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&])(?=.{8,})','Password Regex');
INSERT INTO CONFIGURATION (ID, KEY, VALUE,NOTE) VALUES (13, 'PASSWORD_REGEX_HINTS_MESSAGE', 'Password Must Be At Least 8 Characters and Can be Up-to 40 Characters
Password Must Contain at Least 1 Number
Password Must Contain at Least 1 Special Character
Password Must Contain at Least 1 Upper-Case Letter
Password Must Contain at Least 1 Lower-Case Letter','Password Regex Hints Message');
