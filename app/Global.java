
import dataManagement.CronMan;
import models.Beach;
import org.joda.time.DateTime;

import play.*;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        try {
            // if database is empty, pre-load database
        	boolean databaseReset = databaseEmpty();
            if (databaseReset) {
                readInBeaches(); // read in beaches
            }

            // If test mode, make fake data.  Otherwise, start scraping cron job
            if (controllers.Application.config.isTestMode()) {
                if (databaseReset) {
                	//makeFakeData();
                }
            }
            else {
                startScrapingCron();
            }
        }
        catch (Exception e) {
            // if unable to start the program, print to console and exit.
            e.printStackTrace();
            System.exit(-2);
        }
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
        
        // Tell the CronMan that the application is shutting down, so that it does not do a scrape
        // on shutdown
        CronMan.applicationDead = true;
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

    private void startScrapingCron() {
        DateTime now = new DateTime();

        int minutesOfHour = now.getMinuteOfHour();
        int minutesTillStart;
        if (minutesOfHour <= 15)
            minutesTillStart = 15 - minutesOfHour;
        else if (minutesOfHour <= 30)
            minutesTillStart = 30 - minutesOfHour;
        else if (minutesOfHour <= 45)
            minutesTillStart = 45 - minutesOfHour;
        else
            minutesTillStart = 60 - minutesOfHour;

        System.out.println("Starting scraping CPD in " + minutesTillStart + " minutes");

        CronMan.scrapeBeachSites(minutesTillStart);
    }

}