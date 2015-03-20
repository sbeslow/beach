package webScraping;

import models.Beach;
import models.BeachSnapshot;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public abstract class BeachScraper {

    public static void scrapeCpdPage(Beach beach) throws Exception {

        Document doc;

        String cpdUrl = "http://www.cpdbeaches.com/beaches/" + beach.urlCode;
        doc = Jsoup.connect(cpdUrl).get();

        BeachSnapshot beachSnapshot = new BeachSnapshot(beach);

        Element bSnap = doc.getElementsByClass("beach-snapshot").first();
        Element restrictions = bSnap.getElementsByClass("status").first();
        List<String> restrictTags = new ArrayList<>(restrictions.classNames());

        // TODO: Catch exceptions here and don't completely fail this whole procedure
        String restriction = restrictTags.get(0);
        if (restriction.equals("status")) {
            restriction = restrictTags.get(1);
        }

        if (restriction.equals("no-restrictions")) {
            beachSnapshot.swimStatus = BeachSnapshot.NO_RESTRICTIONS;
        }
        else if (restriction.contains("adv")) {
            beachSnapshot.swimStatus = BeachSnapshot.SWIM_ADVISORY;
        }
        else if (restriction.contains("ban")) {
            beachSnapshot.swimStatus = BeachSnapshot.SWIM_BAN;
        }
        else {
            beachSnapshot.swimStatus = "ERROR";
        }

        Elements tableRows = bSnap.getElementsByTag("td");
        beachSnapshot.forecastForToday = Integer.parseInt(tableRows.get(1).childNode(0).toString().trim());
        beachSnapshot.mostRecentResult = Integer.parseInt(tableRows.get(3).childNode(0).toString().trim());
        beachSnapshot.resultCollected = tableRows.get(4).childNode(0).toString();
        beachSnapshot.scrapeTime = new DateTime();

        beachSnapshot.save();
    }
}
