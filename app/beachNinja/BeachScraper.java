package beachNinja;

import models.Beach;
import models.BeachSnapshot;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class BeachScraper {

    private static final Logger.ALogger logger = Logger.of(BeachScraper.class);

    private static void scrapeCpdPage(Beach beach) throws Exception {
    	
    	System.out.println("Scraping beach: " + beach.name);

        Document doc;

        String cpdUrl = "http://www.cpdbeaches.com/beaches/" + beach.urlCode;
        doc = Jsoup.connect(cpdUrl).get();

        BeachSnapshot beachSnapshot = new BeachSnapshot(beach);

        Element bSnap = doc.getElementsByClass("beach-snapshot").first();
        Element restrictions = bSnap.getElementsByClass("status").first();
        List<String> restrictTags = new ArrayList<>(restrictions.classNames());

        try {

            String restriction = restrictTags.get(0);
            if (restriction.equals("status")) {
                restriction = restrictTags.get(1);
            }

            if (restriction.equals("no-restrictions")) {
                beachSnapshot.swimStatus = BeachSnapshot.NO_RESTRICTIONS;
            } else if (restriction.contains("adv")) {
                beachSnapshot.swimStatus = BeachSnapshot.SWIM_ADVISORY;
            } else if (restriction.contains("ban")) {
                beachSnapshot.swimStatus = BeachSnapshot.SWIM_BAN;
            } else {
                beachSnapshot.swimStatus = "ERROR";
            }
        }
        catch (Exception e) {
            logger.error("for beach: " + beach.name + "\n" + ExceptionUtils.getStackTrace(e));
            beachSnapshot.swimStatus = "ERROR";
        }

        Elements tableRows = bSnap.getElementsByTag("td");
        try {
            beachSnapshot.forecastForToday = Integer.parseInt(tableRows.get(1).childNode(0).toString().trim());
        }
        catch (Exception e) {
            beachSnapshot.forecastForToday = null;
            logger.error("for beach: " + beach.name + "\n" + ExceptionUtils.getStackTrace(e));
        }
        try {
            beachSnapshot.mostRecentResult = Integer.parseInt(tableRows.get(3).childNode(0).toString().trim());
        }
        catch (Exception e) {
            beachSnapshot.mostRecentResult = null;
            logger.error("for beach: " + beach.name + "\n" + ExceptionUtils.getStackTrace(e));
        }

        try {
            beachSnapshot.resultCollected = tableRows.get(4).childNode(0).toString();
        }
        catch (Exception e) {
            logger.error("for beach: " + beach.name + "\n" + ExceptionUtils.getStackTrace(e));
        }
        beachSnapshot.scrapeTime = new DateTime();

        beachSnapshot.save();
    }

    public static void scrapeAllCpdPages() {
        List<Beach> beaches = Beach.find.all();
        for (Beach beach : beaches) {
            try {
                scrapeCpdPage(beach);
            }
            catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
