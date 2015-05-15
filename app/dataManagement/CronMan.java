package dataManagement;

import beachNinja.BeachScraper;
import helpers.TimeUtils;
import models.SignificantError;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

// This class handles cron jobs, which are done using Akka
public abstract class CronMan {

    private static final Logger.ALogger logger = Logger.of(CronMan.class);

    // Cron job that scrapes CPD beach sites
    public static void scrapeBeachSites() {

        try {

            DateTime now = new DateTime();

            LocalTime sevenPm = new LocalTime(19, 0, 0);
            int minutesToStartScraping = 0;
            if (now.plusMinutes(2).toLocalTime().isAfter(sevenPm)) {
                LocalDate tomorrow = now.plusDays(1).toLocalDate();
                DateTime elevenAmTomorrow = new DateTime(tomorrow.getYear(), tomorrow.getMonthOfYear(),
                        tomorrow.getDayOfMonth(), 11, 0, 0);

                minutesToStartScraping = TimeUtils.minutesDifference(now, elevenAmTomorrow);
            }


            FiniteDuration delay = FiniteDuration.create(minutesToStartScraping, TimeUnit.MINUTES);
            FiniteDuration frequency = FiniteDuration.create(15, TimeUnit.MINUTES);

            Runnable showTime = new Runnable() {
                @Override
                public void run() {

                    DateTime now = new DateTime();

                    LocalTime sevenPm = new LocalTime(19, 0, 0);

                    if (now.toLocalTime().isBefore(new LocalTime(11, 0, 0))) {
                        return;
                    }
                    logger.info("Running scrape");
                    // TODO: Check time and don't run after 7:00PM
                    BeachScraper.scrapeAllCpdPages();
                }
            };

            Akka.system().scheduler().schedule(delay, frequency, showTime, Akka.system().dispatcher());
        }
        catch (Exception e) {
            SignificantError.write(e);
            e.printStackTrace();
        }
    }
}
