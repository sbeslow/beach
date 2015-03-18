package webScraping;

import models.Beach;
import models.BeachSnapshot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BeachScraper {

    public static Map<Long, Beach> scrapeAllBeachWebsites() {

        // TODO: Do the web scraping here
        // TODO: Save the beaches to the database
        // TODO: Map key should be beach id
        Beach beach = Beach.find.all().get(0);

        try {
            BeachSnapshot bs = scrapeCpdPage(beach);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BeachSnapshot scrapeCpdPage(Beach beach) throws Exception {

        Document doc;

        doc = Jsoup.connect("http://www.cpdbeaches.com/beaches/12th-Street-Beach/").get();

        BeachSnapshot beachSnapshot = new BeachSnapshot(beach);

        Element bSnap = doc.getElementsByClass("beach-snapshot").first();
        Element restrictions = bSnap.getElementsByClass("status").first();
        List<String> restrictTags = new ArrayList(restrictions.classNames());

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



        return null;
    }

    // Incoming string is <td>blah</td> --> get rid of the tags
    private static String stripTd(String origString) {
        Document doc = Jsoup.parse(origString);
        Element el = doc.getElementsByTag("td").first();
        return el.toString();
    }
}
