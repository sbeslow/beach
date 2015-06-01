// The real

package models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import scala.reflect.internal.TypeDebugging;
import scoreboard.BeachRanking;

import java.util.*;

public class Scoreboard {

    // Note, this stuff is public on purpose.  Besides

    public long calculatedTime;
    public List<BeachRanking> beachRankings;

    public List<BeachRanking> getBeachRankings() {
        return beachRankings;
    }

    public Scoreboard() {

        List<Beach> beaches = Beach.find.all();
        Map<Double, List<Beach>> scoreMap = new HashMap<>(beaches.size());

        for (Beach beach : beaches) {
            double poopScore = beach.poopScore();

            List<Beach> beachesWithThisScore = scoreMap.get(poopScore);
            if (null == beachesWithThisScore) {
                beachesWithThisScore = new ArrayList<>(beaches.size());
            }
            beachesWithThisScore.add(beach);
            scoreMap.put(poopScore, beachesWithThisScore);
        }

        List<Double> scores = new ArrayList<>(scoreMap.keySet());
        Collections.sort(scores);
        Collections.reverse(scores);

        int rank = 1;

        beachRankings = new ArrayList<>(beaches.size());

        for (Double score : scores) {
            List<Beach> beachesWithScore = scoreMap.get(score);
            for (Beach beach : beachesWithScore) {
                beachRankings.add(new BeachRanking(beach, rank));
            }
            rank++;
        }
        calculatedTime = new DateTime().getMillis();

    }

    public List<BeachRanking> topTen() {
        List<BeachRanking> retVal = new ArrayList<>();
        int counter = 0;
        int previousRank = 0;

        for (BeachRanking beachRanking : beachRankings) {
            if ((beachRanking.getRank() > 10) ||
                    ((counter >= 10) && (previousRank != beachRanking.getRank()))) {
                return retVal;
            }
            retVal.add(beachRanking);
            counter++;
            previousRank = beachRanking.getRank();
        }

        return retVal;
    }

    public String asOf() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM, yyyy");
        return formatter.print(calculatedTime);
    }
}
