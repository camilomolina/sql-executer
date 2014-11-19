package cl.bennu.labs4.scheluder;

import cl.bennu.labs4.sql.SQLJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import java.text.ParseException;
import java.util.ResourceBundle;

/**
 * Created by _Camilo on 19-06-2014.
 */
public class SQLScheluder {

    private static Log log = LogFactory.getLog(SQLScheluder.class);

    private static SchedulerFactory schedulerFactory;
    private static Scheduler scheduler;
    private static SQLScheluder sqlScheluder = new SQLScheluder();

    static {
        try {
            log.debug("Creando scheluder");
            schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private SQLScheluder() {
        init();
    }

    public static SQLScheluder getInstance() {
        return sqlScheluder;
    }

    private static void init() {
        try {
            schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();

            JobDetail jobDetail = new JobDetail("sqlJob", Scheduler.DEFAULT_GROUP, SQLJob.class);
            CronTrigger trigger = new CronTrigger("cronTrigger", Scheduler.DEFAULT_GROUP, ResourceBundle.getBundle("init").getString("bennu.cron.expression"));

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.getStackTrace();
        } catch (ParseException e) {
            e.getStackTrace();
        }
    }

    public void start() {
        try {
            log.debug("partiendo jobs");
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            if (!scheduler.isShutdown()) {
                log.debug("parando jobs");
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
