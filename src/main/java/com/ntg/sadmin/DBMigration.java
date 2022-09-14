package com.ntg.sadmin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.reflections.vfs.Vfs;
import org.reflections.vfs.Vfs.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.ntg.sadmin.common.NTGMessageOperation;
import com.ntg.sadmin.config.tenant.DataSourceBasedMultiTenantConnectionProviderImpl;
import com.ntg.sadmin.config.tenant.MultitenancyConfigurationProperties;

public class DBMigration implements Runnable {

    String SchemaName;

    private static final Logger logger = LoggerFactory.getLogger(DBMigration.class);

    public static DBMigration DBMigrationInst;

    private final DataSourceBasedMultiTenantConnectionProviderImpl dsMtCpi;

    private final MultitenancyConfigurationProperties mcP;

    public static List<String> keys = new ArrayList<>(3000);

    public DBMigration(MultitenancyConfigurationProperties mcP,
                       DataSourceBasedMultiTenantConnectionProviderImpl dsMtCpi, String in_SchemaName) {
        //Dev-00003306:Migration Script table should have schema name to avoid existence in other schemas
        this.SchemaName =(in_SchemaName ==null || in_SchemaName.equals(""))? "" : (in_SchemaName + ".");
        this.mcP = mcP;
        this.dsMtCpi = dsMtCpi;
        DBMigration.DBMigrationInst = this;

    }

    public void StartDBMigrationJobs() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            for (MultitenancyConfigurationProperties.Tenant obj : mcP.getTenants()) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dsMtCpi.selectDataSource(obj.getName()));
                Migrate(jdbcTemplate);
            }
        } catch (Exception e) {
            logger.error("Got exception when run scripts");
            NTGMessageOperation.PrintErrorTrace(e);
        }
    }

    URL ScriptURLurl;

    public void Migrate(JdbcTemplate jdbcTemplate) throws Exception {
        Connection con = jdbcTemplate.getDataSource().getConnection();
        String DBPath = con.getMetaData().getDatabaseProductName().toLowerCase();
        con.close();
        if (ScriptURLurl == null) { // find the scripts folder
            logger.info("Searching For Scripts in Resource/CRMscripts");
            ScriptURLurl = Application.class.getClassLoader().getResource("CRMscripts/" + DBPath);
            logger.info("ScriptURLurl .... [" + ScriptURLurl + "]");

        }

        LoadExecutedScriptHistotry(jdbcTemplate);
        if (ScriptURLurl.getPath().contains(".jar")) {
            URL[] files = listFilesFromJar(DBPath);
            ExecuteScriptsFiles(files, jdbcTemplate);
            ExecutedScripts.clear();
            System.out.println("Info Migrate IsDone For Crm " + " /*");
            return;
        }
        URL[] files = file_listFiles(this.ScriptURLurl);
        if (files.length > 0) {
            ExecuteScriptsFiles(files, jdbcTemplate);
            ExecutedScripts.clear();
            logger.info("Info Migrate IsDone For Crm " + " /*");
        } else {
            logger.info("Info Migrate Scripts folder is Empty " + " /*");
        }
    }

    private URL[] file_listFiles(URL url) throws IOException {
        ArrayList<URL> reList = new ArrayList<URL>();

        if (url.toString().startsWith("vfs:")) {
            Dir vfs = Vfs.fromURL(url);
            String vfsPath = vfs.getPath();
            logger.info("vfs.getPath() ->" + vfsPath);
            File f = new File(vfsPath);
            File[] flist = f.listFiles();
            for (File ff : flist) {
                URL newurl = new URL(url.toString() + "/" + ff.getName());
                reList.add(newurl);
            }
        } else {

            String ResourceName = url.getFile();
            int n = ResourceName.indexOf("CRMscripts");
            ResourceName = ResourceName.substring(n);
            InputStream ins;
            ins = Application.class.getClassLoader().getResourceAsStream(ResourceName);

            Scanner scanner = new Scanner(ins);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                URL newurl = new URL(url.toString() + "/" + line);
                reList.add(newurl);
            }
            scanner.close();
        }
        URL[] rl = new URL[reList.size()];
        rl = reList.toArray(rl);
        reList.clear();
        return rl;
    }

    private final HashMap<String, Boolean> ExecutedScripts = new java.util.HashMap<>();

    private void LoadExecutedScriptHistotry(JdbcTemplate jdbcTemplate) throws Exception {
        try {
            String Sql = "Select Version || '_' || file_name as key from " + SchemaName + "mig_upgrade_history";
            jdbcTemplate.query(Sql, new ResultSetExtractor<java.util.HashMap<String, Boolean>>() {

                @Override
                public java.util.HashMap<String, Boolean> extractData(ResultSet rs)
                        throws SQLException, DataAccessException {
                    while (rs.next()) {
                        ExecutedScripts.put(rs.getString("key"), true);
                    }
                    return ExecutedScripts;
                }
            });
        } catch (Exception ex) {
            // mean table of not exists
            String Sql = "CREATE TABLE " + SchemaName + "mig_upgrade_history(" + "Version character varying(15), "
                    + "file_name character varying(255)," + "execution_date timestamp with time zone NOT NULL,"
                    + "CONSTRAINT mig_scripthistory_prm PRIMARY KEY (Version, file_name)" + ")";
            try {
                logger.info("Info:Run Script : ");
                logger.info(Sql);
                jdbcTemplate.execute(Sql);
            } catch (Exception e) {
                logger.error("Info Migrate --> " + e.getMessage());
            }
            logger.info("Info:*/ : ");
        }
        try {
            String Sql = "Select '1' test from " + SchemaName + "MIG_UPGRADE_HISTORY_log";
            jdbcTemplate.query(Sql, new ResultSetExtractor<java.util.HashMap<String, Boolean>>() {

                @Override
                public java.util.HashMap<String, Boolean> extractData(ResultSet rs)
                        throws SQLException, DataAccessException {
                    if (rs.next()) {
                        rs.getString("test");
                    }
                    return null;
                }
            });
        } catch (Exception ex) {
            // mean table of not exists
            String Sql = "CREATE TABLE " + SchemaName + "MIG_UPGRADE_HISTORY_log(" + "Version character varying(15), "
                    + "file_name character varying(255),"
                    + "execution_date timestamp with time zone NOT NULL,script character varying(4000),result character varying(4000),comments character varying(4000))";
            try {
                logger.info("Info:Run Script : ");
                logger.info(Sql);
                jdbcTemplate.execute(Sql);
            } catch (Exception e) {
                logger.error("Info Migrate --> " + e.getMessage());
            }
            logger.info("Info:*/ : ");
        }
    }

    Comparator<URL> comprator = new Comparator<URL>() {
        @Override
        public int compare(URL o1, URL o2) {
            return o1.getFile().compareTo(o2.getFile());
        }
    };

    public void ExecuteScriptsFiles(URL[] files, JdbcTemplate jdbcTemplate) throws Exception {
        if (files != null) {

            Arrays.sort(files, comprator);
            for (URL file : files) {
                if (file_isDirectory(file)) {
                    URL[] folderFiles = file_listFiles(file);
                    logger.info("Directory: " + file_getName(file));
                    ExecuteScriptsFiles(folderFiles, jdbcTemplate);
                } else {
                    String[] list = file.getPath().split("/");
                    String Version = (list.length - 2 > -1) ? list[list.length - 2] : "1";
                    String fileName = file_getName(file);
                    String key = Version + "_" + fileName;
                    String f = fileName.toLowerCase();
                    if (ExecutedScripts.get(key) == null && (f.endsWith(".txt") || f.endsWith(".sql"))) {
                        System.err.println("Execute File: " + file_getName(file));
                        logger.info("Execute File: " + file_getName(file));
                        ExecuteScritps(file, fileName, Version, jdbcTemplate);
                    }
                }
            }
        }
    }

    private boolean file_isDirectory(URL file) {

        String Name = file_getName(file).toLowerCase();
        boolean re = (!Name.endsWith(".txt") && !Name.endsWith(".sql"));
        if (re) {
            try {
                // test the folder
                URL[] test = file_listFiles(file);
                test[0].openStream();
            } catch (Exception e) {
                // not a folder and it was file
                re = false;
            }
        }
        return re;
    }

    private String file_getName(URL file) {
        String[] list = file.getFile().split("/");
        return list[list.length - 1];
    }

    private void ExecuteScritps(URL file, String fileName, String Version, JdbcTemplate jdbcTemplate) throws Exception {
        String ResourceName = file.getFile();
        int n = ResourceName.indexOf("CRMscripts");
        ResourceName = ResourceName.substring(n);
        InputStream ins = null;

        ins = Application.class.getClassLoader().getResourceAsStream(ResourceName);

        Scanner scanner = new Scanner(ins);
        StringBuilder fileContent = new StringBuilder();
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine());
            fileContent.append("\r\n");
        }
        scanner.close();
        if (fileContent != null) {

            String[] queries = null;

            if (fileContent.toString().trim().toUpperCase().startsWith("DECLARE")) {
                queries = new String[1];
                queries[0] = fileContent.toString();
            } else
                queries = fileContent.toString().split(";");

            String Sql = null;
            String RemovingAction = null;
            String LastComments = "";
            for (int i = 0; i < queries.length; i++) {
                String ExecuationOutput = "";
                Sql = null;
                try {
                    queries[i] = queries[i].replaceAll("[^\\x00-\\x7F]", "").trim();
                    String check = queries[i].replaceAll("\r\n", "").trim().toLowerCase();
                    if (check.startsWith("--")) {
                        // mean comments
                        if (check.startsWith("--begin")) {
                            Sql = "";
                            for (i++; i < queries.length; i++) {
                                check = queries[i].replaceAll("\r\n", "").trim().toLowerCase();
                                if (check.startsWith("--")) {
                                    if (check.startsWith("--end")) {
                                        break;
                                    }
                                } else {
                                    Sql += queries[i] + ";";
                                }
                            }
                        } else if (check.startsWith("--onerror remove")) {
                            RemovingAction = queries[i].substring(17);
                        } else if (check.startsWith("--end")) {
                            RemovingAction = null;
                        } else {
                            LastComments = queries[i].trim();
                        }
                    } else {
                        Sql = queries[i].trim();
                    }
                    if (Sql != null && !Sql.equals("")) {
                        try {
                            jdbcTemplate.execute(Sql);
                        } catch (Exception e) {
                            if (RemovingAction == null) {
                                throw e;
                            } else {
                                // try again with removing some text
                                jdbcTemplate.execute(Sql.replaceAll(RemovingAction, " "));
                            }
                        }
                        ExecuationOutput = "Ok";
                    }

                } catch (Exception e) {
                    logger.error("Info Migrate --> " + e.getMessage() + " For /*");
                    logger.error(Sql);
                    logger.error("*/");
                    ExecuationOutput = e.getMessage().replaceAll("'", "''");
                }

                if (Sql != null && !Sql.equals("")) {
                    if (Sql.length() > 4000) {
                        Sql = Sql.substring(0, 3995) + "...";
                    }
                    if (fileName.length() > 255) {
                        fileName = fileName.substring(0, 250) + "...";
                    }
                    String fname = fileName.replaceAll("'", "''");

                    if (LastComments.length() > 4000) {
                        LastComments = LastComments.substring(0, 3995) + "...";
                    }
                    LastComments = LastComments.replaceAll("'", "''");

                    try {
                        jdbcTemplate.update(
                                "insert into " + SchemaName + "MIG_UPGRADE_HISTORY_log(Version,file_name,execution_date,script,result,comments) values('"
                                        + Version + "','" + fname + "',localtimestamp,'" + Sql.replaceAll("'", "''")
                                        + "','" + ExecuationOutput + "','" + LastComments + "')");
                    } catch (Exception e) {
                        logger.error("DBMig:Error in Log -->" + e.getMessage());
                    }
                }
            }
            jdbcTemplate.update("insert into " + SchemaName + "mig_upgrade_history(Version,file_name,execution_date) values('" + Version
                    + "','" + fileName.replaceAll("'", "''") + "',localtimestamp)");
            logger.info("Execute File: " + file_getName(file) + " Done*/");
        }
    }

    public URL[] listFilesFromJar(String DBPath) {
        ArrayList<URL> reList = new ArrayList<URL>();
        CodeSource src = Application.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip;
            try {
                zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    // System.out.println(e);
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith("BOOT-INF/classes/CRMscripts/" + DBPath) && (name.endsWith(".txt") || name.endsWith(".sql"))) {
                        URL newurl = new URL(jar.toString() + "/" + name);
                        reList.add(newurl);
                        //System.out.println(newurl.getPath());
                    } else {
                        continue;
                    }
                }
                URL[] rl = new URL[reList.size()];
                rl = reList.toArray(rl);
                reList.clear();
                return rl;
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return null;
    }

}
