import models.Beach;
import play.*;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        try {
            // if database is empty, fill it with fake data
            if (databaseEmpty()) {
                readInBeaches();
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

        // Get rid of the header row
        String sCurrentLine = br.readLine();
        try {

            while ((sCurrentLine = br.readLine()) != null) {
                String[] beachFields = sCurrentLine.split(",");
                Beach beach = new Beach(beachFields);
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
        if (0 == beaches.size())
            return true;
        return false;
    }
}