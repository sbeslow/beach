import beachNinja.BeachScraper;
import models.Beach;
import models.BeachSnapshot;
import models.SignificantError;
import org.joda.time.DateTime;
import play.*;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        try {
            // if database is empty, pre-load database
            if (databaseEmpty()) {
                readInBeaches(); // read in beaches
                //createFakeData();
            }
            scrapeCron();
            if (controllers.Application.config.isTestMode()) {
                makeFakeData();
            }
        }
        catch (Exception e) {
            // if unable to start the program, print to console and exit.
            e.printStackTrace();
            System.exit(-2);
        }
    }

    private void makeFakeData() {
        List<Beach> beachList = Beach.find.all();

        for (Beach beach : beachList) {
            DateTime timeMarker = new DateTime();
            for (int i = 0; i < 24; i ++) {
                timeMarker = timeMarker.minusHours(1);
                Random rand = new Random();
                int randomNum = rand.nextInt((6 - 1) + 1) + 1;
                String swimStatus = BeachSnapshot.NO_RESTRICTIONS;
                if (randomNum == 1)
                    swimStatus = BeachSnapshot.SWIM_BAN;
                else if (randomNum <= 3)
                    swimStatus = BeachSnapshot.SWIM_ADVISORY;

                BeachSnapshot snapshot = new BeachSnapshot(timeMarker, beach, swimStatus, 0, 0, null);
                snapshot.save();
            }
        }
    }

    public void onStop(Application app) {
        //Logger.info("Application shutdown...");
    }

    private void readInBeaches() throws Exception {

        File theFile = Play.application().getFile("conf/beaches.csv");

        BufferedReader br = new BufferedReader(new java.io.FileReader(theFile));

        String sCurrentLine;
        br.readLine(); // Get rid of the header row
        try {

            while ((sCurrentLine = br.readLine()) != null) {
                String[] beachFields = sCurrentLine.split(",");

                Beach beach;
                try {
                    beach = new Beach(beachFields);
                }
                catch(Exception e) {
                    continue;
                }
                beach.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Check if the database is empty by seeing if there are any beaches in it.
    private boolean databaseEmpty() {
        List<Beach> beaches = Beach.find.all();
        return 0 == beaches.size();
    }

    // Create some fake data for testing purposes.
    private void createFakeData() {

        Beach beach = Beach.find.all().get(0);

        DateTime now = new DateTime();

        (new BeachSnapshot(now.minusHours(1), beach, BeachSnapshot.NO_RESTRICTIONS, 10, 12, "Aug 12")).save();
        (new BeachSnapshot(now, beach, BeachSnapshot.SWIM_ADVISORY, 12, 15, "Aug 11")).save();

    }

    // Cron job that scrapes CPD site
    private void scrapeCron() {

        try {

            DateTime now = new DateTime();
            DateTime inAnHour = now.plusHours(1);
            DateTime nextHour = new DateTime(inAnHour.getYear(), inAnHour.getMonthOfYear(), inAnHour.getDayOfMonth(),
                    inAnHour.getHourOfDay(), 0, inAnHour.getSecondOfMinute());
            int secondsTillTopOfHour = ((int) (nextHour.getMillis() - now.getMillis())) / 1000;

            FiniteDuration delay = FiniteDuration.create(secondsTillTopOfHour, TimeUnit.SECONDS);
            FiniteDuration frequency = FiniteDuration.create(30, TimeUnit.MINUTES);

            Runnable showTime = new Runnable() {
                @Override
                public void run() {
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