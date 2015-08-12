
package models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import scoreboard.EcoliMeasurement;

import java.util.*;

public class Scoreboard {

    public long calculatedTime;
    public List<BeachRanking> beachRankings;

    public Scoreboard() {

        List<Beach> beaches = Beach.find.all();
        Map<Double, List<BeachRanking>> scoreMap = new HashMap<>(beaches.size());

        for (Beach beach : beaches) {
        	
        	BeachRanking beachRanking = new BeachRanking(beach);

            List<BeachRanking> beachesWithThisScore = scoreMap.get(beachRanking.poopScore);
            if (null == beachesWithThisScore) {
                beachesWithThisScore = new ArrayList<>(beaches.size());
            }
            beachesWithThisScore.add(beachRanking);
            scoreMap.put(beachRanking.poopScore, beachesWithThisScore);
        }

        List<Double> scores = new ArrayList<>(scoreMap.keySet());
        Collections.sort(scores);
        Collections.reverse(scores);

        int rank = 1;

        beachRankings = new ArrayList<>(beaches.size());

        for (Double score : scores) {
            List<BeachRanking> beachesWithScore = scoreMap.get(score);
            for (BeachRanking beachRanking : beachesWithScore) {
            	beachRanking.rank = rank;
                beachRankings.add(beachRanking);
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
            if ((beachRanking.rank > 10) ||
                    ((counter >= 10) && (previousRank != beachRanking.rank))) {
                return retVal;
            }
            retVal.add(beachRanking);
            counter++;
            previousRank = beachRanking.rank;
        }

        return retVal;
    }

    public String asOf() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM, yyyy");
        return formatter.print(calculatedTime);
    }
    
    public String percentOfDaysOver1000() {
    	int daysWithCorrectStatus = 0;
    	int totalDays = 0;
    	for (BeachRanking beachRanking : beachRankings) {
    		
    		for (EcoliMeasurement ecoliMeasurement : beachRanking.ecoliMeasurements) {
    			if (ecoliMeasurement.getReading() >= 1000) {
    				if (ecoliMeasurement.getMaxSwimStatus().equals(BeachSnapshot.SWIM_BAN)) {
    					daysWithCorrectStatus++;
    				}
        			totalDays++;
    			}
    		}
    		
    	}
    	
    	double percent = 100 * (double) daysWithCorrectStatus / totalDays;
    	return String.format("%.1f", percent);
    }
    
    public String percentOfDaysOver235() {
    	int daysWithCorrectStatus = 0;
    	int totalDays = 0;
    	for (BeachRanking beachRanking : beachRankings) {
    		
    		for (EcoliMeasurement ecoliMeasurement : beachRanking.ecoliMeasurements) {
    			if ((ecoliMeasurement.getReading() < 1000) && (ecoliMeasurement.getReading() >= 235)) {
    				if (!ecoliMeasurement.getMaxSwimStatus().equals(BeachSnapshot.NO_RESTRICTIONS)) {
    					daysWithCorrectStatus++;
    				}
        			totalDays++;
    			}
    		}
    		
    	}
    	
    	double percent = 100 * (double) daysWithCorrectStatus / totalDays;
    	return String.format("%.1f", percent);
    }
}
