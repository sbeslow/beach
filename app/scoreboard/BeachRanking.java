package scoreboard;

import java.io.Serializable;
import java.text.DecimalFormat;

public class BeachRanking {

    private int rank;

    private String beachName;
    private String beachUrl;

    private double poopScore;

    public int getRank() {
        return rank;
    }

    public String getBeachName() {
        return beachName;
    }

    public String getBeachUrl() {
        return beachUrl;
    }

    public String getPoopScore() {
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(poopScore);
    }

    public BeachRanking(int rank, String beachName, String beachUrl, double poopScore) {
        this.rank = rank;
        this.beachName = beachName;
        this.poopScore = poopScore;
        this.beachUrl = beachUrl;
    }
}
