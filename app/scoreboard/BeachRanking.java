package scoreboard;

import models.Beach;

import java.text.DecimalFormat;
import java.util.List;

public class BeachRanking {

    private int rank;

    private String beachName;
    private String beachUrl;

    private double poopScore;

    private String currentStatus;

    public List<PoopDay> poopDays;

    public BeachRanking(Beach beach, int rank) {
        this.rank = rank;
        this.beachName = beach.name;
        this.poopScore = beach.poopScore();
        this.beachUrl = beach.urlCode;
        this.poopDays = beach.poopDays();
        this.currentStatus = beach.currentStatus();
    }

    public int getRank() {
        return rank;
    }

    public String getBeachName() {
        return beachName;
    }

    public String getBeachUrl() {
        return beachUrl;
    }

    public double getPoopScore() {
        return poopScore;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public List<PoopDay> getPoopDays() {
        return poopDays;
    }

}
