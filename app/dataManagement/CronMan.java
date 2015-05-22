package dataManagement;

import beachNinja.BeachScraper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

// This class handles cron jobs, which are done using Akka
public abstract class CronMan {

    private static final Logger.ALogger logger = Logger.of(CronMan.class);

    // Cron job that scrapes CPD beach sites
    public static void scrapeBeachSites(int minutesToStart) {

        try {

            FiniteDuration delay = FiniteDuration.create(minutesToStart, TimeUnit.MINUTES);
            FiniteDuration frequency = FiniteDuration.create(15, TimeUnit.MINUTES);

            Runnable runScraper = new Runnable() {
                @Override
                public void run() {

                    DateTime now = new DateTime();

                    // Only collect between11 AM and 7PM.  Allow 2 minute buffer after 7:00
                    if ((now.toLocalTime().isBefore(new LocalTime(11, 0, 0))) ||
                            (now.toLocalTime().isAfter(new LocalTime(19, 2, 0)))) {
                        return;
                    }
                    logger.info("Running scrape");
                    // TODO: Check time and don't run after 7:00PM
                    BeachScraper.scrapeAllCpdPages();
                }
            };

            Akka.system().scheduler().schedule(delay, frequency, runScraper, Akka.system().dispatcher());


        }
        catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
