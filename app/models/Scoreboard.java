package models;

import org.joda.time.DateTime;
import scoreboard.BeachRanking;

import java.util.*;

public class Scoreboard {

    private long calculatedTime;
    private List<BeachRanking> beachRankings;

    public long getCalculatedTime() {
        return calculatedTime;
    }

    public List<BeachRanking> getBeachRankings() {
        return beachRankings;
    }

    public Scoreboard() {

        List<Beach> beaches = Beach.find.all();
        Map<Double, List<Beach>> scoreMap = new HashMap<>(beaches.size());

        for (Beach beach : beaches) {
            double pooScore = beach.pooScore();

            List<Beach> beachesWithThisScore = scoreMap.get(pooScore);
            if (null == beachesWithThisScore) {
                beachesWithThisScore = new ArrayList<>(beaches.size());
            }
            beachesWithThisScore.add(beach);
            scoreMap.put(pooScore, beachesWithThisScore);
        }

        List<Double> scores = new ArrayList<>(scoreMap.keySet());
        Collections.sort(scores);
        Collections.reverse(scores);

        int rank = 1;

        beachRankings = new ArrayList<>(beaches.size());

        for (Double score : scores) {
            List<Beach> beachesWithScore = scoreMap.get(score);
            for (Beach beach : beachesWithScore) {
                beachRankings.add(new BeachRanking(rank, beach.name, beach.urlCode, score));
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
}
