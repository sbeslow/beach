package beachNinja;

import controllers.Application;
import models.Beach;
import models.BeachSnapshot;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import play.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class BeachScraper {

    private static final Logger.ALogger logger = Logger.of(BeachScraper.class);

    public static void scrapeCpdPage(Beach beach) throws Exception {

        Document doc;

        String cpdUrl = "http://www.cpdbeaches.com/beaches/" + beach.urlCode;
        doc = Jsoup.connect(cpdUrl).get();

        BeachSnapshot beachSnapshot = parseHtml(doc, beach);

        beachSnapshot.save();
    }

    private static BeachSnapshot parseHtml(Document doc, Beach beach) {

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
            if (beach.id != 131) // Humboldt
                logger.error("for beach: " + beach.name + "\n" + ExceptionUtils.getStackTrace(e));
            beachSnapshot.swimStatus = "ERROR";
        }

        Elements tableRows = bSnap.getElementsByTag("td");
        try {
            beachSnapshot.forecastForToday = Double.parseDouble(tableRows.get(1).childNode(0).toString().trim());
        }
        catch (Exception e) {
            beachSnapshot.forecastForToday = null;
            if (beach.id != 131) // Humboldt
                logger.error("for beach: " + beach.name + "\n" + ExceptionUtils.getStackTrace(e));
        }

        String mostRecResultStr = tableRows.get(3).childNode(0).toString();
        mostRecResultStr = mostRecResultStr.replace(" CCE", "");
        try {
            beachSnapshot.mostRecentResult = Double.parseDouble(mostRecResultStr.trim());

        }
        catch (Exception e) {
            beachSnapshot.mostRecentResult = null;
            if (beach.id != 131) // Humboldt
                logger.error("for beach: " + beach.name + "\n" + ExceptionUtils.getStackTrace(e));
        }

        try {
            Node resultNode = tableRows.get(4).childNode(0);

            beachSnapshot.resultCollected = resultNode.toString();

            String dateStr = resultNode.childNode(0).childNode(0).toString();
            dateStr = dateStr.replace("sample collected on ", "");
            DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM dd, yyyy");
            beachSnapshot.resultDate = LocalDate.parse(dateStr, fmt);


        }
        catch (Exception e) {
            if (beach.id != 131) // Humboldt
                logger.error("for beach: " + beach.name + " on result node " + beachSnapshot.resultCollected + "\n" + ExceptionUtils.getStackTrace(e));
        }
        beachSnapshot.scrapeTime = new DateTime();

        return beachSnapshot;

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

        Application.updateScoreboard();
    }

    public static BeachSnapshot scrapeCpdHtmlString(String html, Beach beach) {
        Document doc = Jsoup.parse(html);
        return parseHtml(doc, beach);
    }
}
