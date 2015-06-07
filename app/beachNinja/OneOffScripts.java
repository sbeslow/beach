package beachNinja;

import com.google.common.io.Files;
import models.Beach;
import models.BeachSnapshot;
import java.nio.charset.Charset;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneOffScripts {

    public static void fillEmptyForecasts() {

        Map<String, Beach> beachMap = new HashMap<>();
        List<Beach> beaches = Beach.find.all();
        for (Beach beach : beaches) {
            beachMap.put(beach.urlCode, beach);
        }

        File[] files = new File("/Users/scottbeslow/Downloads/scrapedFiles2").listFiles();


        for (File file : files) {

            String fileName = file.getName();
            try {
                int month = Integer.parseInt(fileName.substring(4, 6));
                int day = Integer.parseInt(fileName.substring(6,8));
                int hour = Integer.parseInt(fileName.substring(10,12));
                int minute = Integer.parseInt(fileName.substring(13,15));

                DateTime dateTime = new DateTime(2015, month, day, hour, minute);

                if (dateTime.toLocalDate().isAfter(new LocalDate(2015, 5, 28))) {
                    System.out.println("After date Skipping " + fileName);
                    continue;
                }

                String beachUrl = fileName.substring(15, fileName.length()-5);
                if (beachUrl.equals("CpdBeachHome"))
                        continue;
                else if (beachUrl.equals("Jarvis-Beach"))
                    beachUrl = "Marion-Mahoney-Griffin-Beach";
                Beach beach = beachMap.get(beachUrl);

                BeachSnapshot closestSnapshot = BeachSnapshot.find.where()
                        .ge("scrapeTime", dateTime).lt("scrapeTime", dateTime.plusMinutes(7))
                        .eq("beach.id", beach.id).findUnique();
                if (null == closestSnapshot) {
                    System.out.println("No suitable snap Skipping " + fileName + " beach id " + beach.id +
                    " and dateTime " + dateTime.toString());
                    continue;
                }

                String html = Files.toString(file, Charset.defaultCharset());
                BeachSnapshot newSnapshot = BeachScraper.scrapeCpdHtmlString(html, beach);

                closestSnapshot.forecastForToday = newSnapshot.forecastForToday;
//                System.out.println("Updating beach " + closestSnapshot.beach.name + " on " +
//                    closestSnapshot.scrapeTime.toString() + " from file named " + fileName);

                closestSnapshot.update();
            }
            catch (Exception e) {
                System.out.println("Failed with file " + fileName);
                e.printStackTrace();
                continue;
            }


        }

    }



}
