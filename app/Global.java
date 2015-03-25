import models.Beach;
import models.BeachSnapshot;
import org.joda.time.DateTime;
import play.*;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        try {
            // if database is empty, pre-load database
            if (databaseEmpty()) {
                readInBeaches(); // read in beaches
                createFakeData();
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
}