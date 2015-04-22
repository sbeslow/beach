import beachNinja.BeachScraper;
import models.Beach;
import models.BeachSnapshot;
import models.SignificantError;

import org.joda.time.DateTime;

import play.*;
import play.Logger.ALogger;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Global extends GlobalSettings {
	
	private static final ALogger logger = Logger.of(Global.class);

    public void onStart(Application app) {
        try {
            // if database is empty, pre-load database
        	boolean databaseReset = databaseEmpty();
            if (databaseReset) {
                readInBeaches(); // read in beaches
            }

            // If test mode, make fake data.  Otherwise, start scraping cron job
            if (controllers.Application.config.isTestMode()) {
                if (databaseReset)
                	makeFakeData();
            }
            else {
            	scrapeCron();
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
        
        Random rand = new Random();
        for (Beach beach : beachList) {
            DateTime timeMarker = new DateTime();
            int forecast = rand.nextInt(101) + 1;
            int recentResult = rand.nextInt(101) + 1;
            for (int i = 0; i < 24; i ++) {
                timeMarker = timeMarker.minusHours(1);
                int randomNum = rand.nextInt(6) + 1;
                String swimStatus = BeachSnapshot.NO_RESTRICTIONS;
                if (randomNum == 1)
                    swimStatus = BeachSnapshot.SWIM_BAN;
                else if (randomNum <= 3)
                    swimStatus = BeachSnapshot.SWIM_ADVISORY;

                BeachSnapshot snapshot = new BeachSnapshot(timeMarker, beach, swimStatus, forecast, recentResult, null);
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