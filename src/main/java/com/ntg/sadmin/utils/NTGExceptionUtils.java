package com.ntg.sadmin.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;

import com.ntg.sadmin.exceptions.NTGRestException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.ntg.sadmin.common.NTGMessageOperation;
import com.ntg.sadmin.common.dbcompatibilityhelper.SqlHelper;

@Component
public class NTGExceptionUtils {
    private static final Logger logger = LoggerFactory.getLogger(NTGExceptionUtils.class);

    @Autowired
    private SqlHelper sqlHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Hashtable<Object, Object> ErrorHashing = new Hashtable<>();
    private String tempUse;

    public String GetErrorTrace(Exception ex) {
    	logger.debug("start GetErrorTrace function");
        String M =(ex instanceof NTGRestException)?((NTGRestException)ex).ErrorMessage : ex.getMessage();
        StringBuilder er = new StringBuilder();

        if (M == null) {
            M = "UnKnow Error";
        }

        if (ex instanceof org.springframework.web.client.HttpServerErrorException) {
            M = ((org.springframework.web.client.HttpServerErrorException) ex).getResponseBodyAsString();
        }
        er.append(M);

        Throwable rootEx = ex.getCause();
        while (rootEx != null) {
            if (rootEx.getMessage() != null) {
                er.append("\n");
                er.append(rootEx.getMessage());
            }
            rootEx = rootEx.getCause();
        }
        
        logger.debug("end GetErrorTrace function");
        return GetErrorTrace(er.toString(), ex);
    }


    public String GetErrorTrace(String er, Exception e) {
        try {
            if (sqlHelper.getConnectionType() == 2 && e != null && e instanceof PSQLException) {
                PSQLException psqlex = (PSQLException) e;
                String mesage = psqlex.getServerErrorMessage().toString();
                if (mesage.contains("constraint")) {

                    int n = er.indexOf("constraint [");
                    String ConstName = er.substring(n + 1, er.indexOf("]", n)).split("\\.")[1];

                    String sql = " select coalesce(m.msg ,case when c.contype = 'p' then 'The Value of Primary Key In Table ' ||  t.relname || ' Is Not Valid' "
                            + " when c.contype = 'f' then 'The Value of ' || att.attname || ' is Not Valid' "
                            + " when c.contype = 'u' then 'Duplicated Value in Column ' || att.attname end ) as message "
                            + " from pg_constraint c join pg_class t on  c.conrelid =  t.oid "
                            + " join pg_attribute att on att.attnum  = c.conkey[1] and att.attrelid = t.oid "
                            + " left  outer join COR_UNQ_CONSTRAIN_MSG m on m.constraint_name = c.conname  where c.conname = '"
                            + ConstName + "'";
                    tempUse = null;
                    jdbcTemplate.query(sql, new ResultSetExtractor<HashMap<String, Boolean>>() {

                        @Override
                        public java.util.HashMap<String, Boolean> extractData(ResultSet rs)
                                throws SQLException, DataAccessException {
                            while (rs.next()) {
                                String msg = rs.getString("message");
                                if (msg != null) {
                                    tempUse = msg;
                                } else {
                                    tempUse = psqlex.getServerErrorMessage().toString();
                                }
                            }
                            return null;
                        }
                    });
                    if (tempUse != null) {
                        er = tempUse;
                    }

                } else {
                    String key = psqlex.getSQLState();
                    String Theer = (String) ErrorHashing.get(key);
                    if (Theer != null) {
                        String Sql = "Select ERRORDESC,ECAUSE,EACTION From COR_ALL_ERRORS Where ERRNO Like '" + key
                                + "'";
                        try {
                            jdbcTemplate.query(Sql, new ResultSetExtractor<java.util.HashMap<String, Boolean>>() {

                                @Override
                                public java.util.HashMap<String, Boolean> extractData(ResultSet rs)
                                        throws SQLException, DataAccessException {
                                    while (rs.next()) {
                                        String Theer = ":" + rs.getString("ERRORDESC") + "\nCause:\n   "
                                                + rs.getString("ECAUSE") + "\nAction:\n   " + rs.getString("EACTION");
                                        ErrorHashing.put(key, Theer);
                                    }
                                    return null;
                                }
                            });
                            Theer = (String) ErrorHashing.get(key);
                            if (Theer == null) {
                                Theer = ":NA\nCause:Na\nAction:\n Na";
                            }

                        } catch (Exception ex) {
                            NTGMessageOperation.PrintErrorTrace(ex);
                        }
                    }

                }
            } else {
                if (er != null) {
                    if (er.contains("constraint")) {
                        try {
                            int n = er.indexOf("constraint [");
                            String ConstName = er.substring(n + 1, er.indexOf("]", n)).split("\\.")[1];

                            String ConstrainMesg = (String) ErrorHashing.get(ConstName);
                            if (ConstrainMesg == null) {
                                String Sql = "with  r as (   SELECT  SUBSTR (pop, 2) R_Name\n" + "        \n"
                                        + "      FROM  (     SELECT   SYS_CONNECT_BY_PATH (column_name, ',') pop, LEVEL\n"
                                        + "                         FROM   (SELECT *\n"
                                        + "                                      FROM  USER_CONS_COLUMNS\n"
                                        + "                                     WHERE  constraint_name = '" + ConstName
                                        + "')\n" + "                 START WITH   constraint_name = '" + ConstName
                                        + "'\n" + "                 CONNECT BY   PRIOR column_name < column_name\n"
                                        + "                    ORDER BY   LEVEL DESC)\n" + "     WHERE  ROWNUM = 1)\n"
                                        + "\n" + "SELECT coalesce(m.msg,\n" + "                \n"
                                        + "            case when co.constraint_type = 'P' or co.constraint_type = 'U' then\n"
                                        + "            'The Value of \"'\n" + "            || r.R_Name \n"
                                        + "            || '\" can''t be duplicate'\n" + "           \n"
                                        + "            end) Msg,co.*\n"
                                        + "  FROM  USER_CONSTRAINTS CO,COR_UNQ_CONSTRAIN_MSG m,r\n"
                                        + " WHERE   co.constraint_name='" + ConstName
                                        + "' and  m.constraint_name(+)=co.constraint_name";
                                // ---

                                jdbcTemplate.query(Sql, new ResultSetExtractor<java.util.HashMap<String, Boolean>>() {

                                    @Override
                                    public java.util.HashMap<String, Boolean> extractData(ResultSet rs)
                                            throws SQLException, DataAccessException {
                                        if (rs.next()) {
                                            String ConstrainMesg = rs.getString("msg");
                                            if (ConstrainMesg != null) {
                                                ErrorHashing.put(ConstName, ConstrainMesg);
                                            }
                                        }
                                        return null;
                                    }
                                });
                                ConstrainMesg = (String) ErrorHashing.get(ConstName);
                                if (ConstrainMesg != null) {
                                    er = "Constraint Err:" + ConstrainMesg;
                                    return er;
                                }

                            }

                        } catch (Exception ex) {
                            // er += Theer;
                        }
                    }
                    // else if not return
                    {

                        int n = CheckDbError(er.toUpperCase());
                        if (n > -1) {
                            int m = er.indexOf(":", n);
                            if (m > -1) {

                                // er is the error Number
                                // To Get Description From Db
                                String key = er.substring(n, m);
                                String Theer = (String) ErrorHashing.get(key);
                                if (Theer == null) {
                                    String Sql = "Select ERRORDESC,ECAUSE,EACTION From COR_ALL_ERRORS Where ERRNO Like '"
                                            + er.substring(n, m) + "'";

                                    er = er.substring(n);
                                    try {

                                        jdbcTemplate.query(Sql,
                                                new ResultSetExtractor<java.util.HashMap<String, Boolean>>() {

                                                    @Override
                                                    public java.util.HashMap<String, Boolean> extractData(ResultSet rs)
                                                            throws SQLException, DataAccessException {
                                                        while (rs.next()) {
                                                            String Theer = ":" + rs.getString("ERRORDESC")
                                                                    + "\nCause:\n   " + rs.getString("ECAUSE")
                                                                    + "\nAction:\n   " + rs.getString("EACTION");
                                                            ErrorHashing.put(key, Theer);
                                                        }
                                                        return null;
                                                    }
                                                });

                                        Theer = (String) ErrorHashing.get(key);

                                        if (Theer == null) {

                                            Theer = ":NA\nCause:Na\nAction:\n Na";
                                        }

                                    } catch (Exception ex) {
                                        NTGMessageOperation.PrintErrorTrace(ex);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            NTGMessageOperation.PrintErrorTrace(ex);
        }
        return er;

    }

    public int CheckDbError(String err) {
        int n = -1;
        n = err.indexOf("ORA-");
        if (n > -1) {
            return n;
        }
        n = err.indexOf("DBA-");
        if (n > -1) {
            return n;
        }
        n = err.indexOf("OSD-");
        if (n > -1) {
            return n;
        }
        n = err.indexOf("PCC-");
        if (n > -1) {
            return n;
        }
        n = err.indexOf("PLS-");
        if (n > -1) {
            return n;
        }
        n = err.indexOf("SQL-");
        if (n > -1) {
            return n;
        }
        return n;
    }
}
