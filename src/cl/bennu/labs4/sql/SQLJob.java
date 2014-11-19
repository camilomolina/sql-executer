package cl.bennu.labs4.sql;

import cl.bennu.labs4.comparator.FileComparator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by _Camilo on 19-06-2014.
 */
public class SQLJob implements Job {

    private static Log log = LogFactory.getLog(SQLJob.class);

    private static String PATH = ResourceBundle.getBundle("init").getString("bennu.scheluder.path");
    private static String HOST = ResourceBundle.getBundle("init").getString("bennu.scheluder.host");
    private static String PORT = ResourceBundle.getBundle("init").getString("bennu.scheluder.port");
    private static String DB = ResourceBundle.getBundle("init").getString("bennu.scheluder.db");
    private static String USER = ResourceBundle.getBundle("init").getString("bennu.scheluder.user");
    private static String PASS = ResourceBundle.getBundle("init").getString("bennu.scheluder.pass");
    private static String DRIVER = ResourceBundle.getBundle("init").getString("bennu.scheluder.driver");


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            File file = new File(PATH);
            String[] ext = {"sql"};
            Collection files = FileUtils.listFiles(file, ext, true);

            Collections.sort((List) files, new FileComparator());

            if (files != null) {

                Class.forName(DRIVER);
                Connection connection = DriverManager.getConnection("jdbc:jtds:sqlserver://" + HOST + ":" + PORT + ";DatabaseName=" + DB, USER, PASS);

                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);

                StringBuffer stringBuffer = new StringBuffer();

                Iterator iter = files.iterator();
                while (iter.hasNext()) {
                    Statement statement = null;
                    File sqlFile = (File) iter.next();
                    try {
                        log.info("Ejecutando archivo " + sqlFile.getName());

                        String text = FileUtils.readFileToString(sqlFile, "utf-16");
                        statement = connection.createStatement();

                        // ejecutamos los archivos encontrados
                        statement.execute(text);

                        stringBuffer.append(new Date().toString());
                        stringBuffer.append(" DEBUG ");
                        stringBuffer.append("[").append(sqlFile.getName()).append("] ");
                        stringBuffer.append("Ejecutado");
                        stringBuffer.append("\n");
                        log.info("... Ok");
                    } catch (Exception ex) {
                        stringBuffer.append(new Date().toString());
                        stringBuffer.append(" ERROR ");
                        stringBuffer.append("[").append(sqlFile.getName()).append("] ");
                        stringBuffer.append("Error al ejecutar");
                        stringBuffer.append("\n");
                        stringBuffer.append(ex);
                        stringBuffer.append("\n");

                        log.error("... ERROR (" + ex.getMessage() + ")", ex);
                        if (statement != null) {
                            statement.close();
                        }
                    }
                }

                // guarda archivo de resumen
                String dateFormatter = "Summary[" + year + StringUtils.rightPad("" + month, 2) + StringUtils.rightPad("" + day, 2) + "].log";
                File summary = new File(PATH + "/log/" + dateFormatter);
                FileUtils.writeStringToFile(summary, stringBuffer.toString());

                connection.close();
            }
        } catch (Exception e) {
            log.error("Error General en Job(" + e.getMessage() + ")", e);
        }
    }

}
