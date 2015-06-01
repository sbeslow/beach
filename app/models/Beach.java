package models;

import beachNinja.SeasonalStats;
import play.db.ebean.Model;
import scoreboard.PoopDay;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Beach extends Model {

    @Id
    public long id;

    public String name;
    public String urlCode;

    public Double latitude;
    public Double longitude;

    @OneToMany(mappedBy = "beach", cascade = CascadeType.ALL)
    @OrderBy("scrapeTime")
    public List<BeachSnapshot> snapshots;

    @Transient
    public SeasonalStats seasonalStats = null;

    public Beach(String[] beachFields) throws Exception {

        this.name = beachFields[0];
        this.urlCode = beachFields[1];

        try {
            this.latitude = Double.parseDouble(beachFields[3]);
            this.longitude = Double.parseDouble(beachFields[4]);
        }
        catch(Exception e) {
            this.latitude = null;
            this.longitude = null;
        }

    }

    public static Finder<Long,Beach> find = new Finder<>(
            Long.class, Beach.class
    );

    public double poopScore() {
        return getSeasonalStats().getPoopScore();
    }

    public List<PoopDay> poopDays() {
        return getSeasonalStats().getPoopDays();
    }

    public String currentStatus() {
        return snapshots.get(snapshots.size()-1).swimStatus;
    }

    public SeasonalStats getSeasonalStats() {
        List<BeachSnapshot> snapshots = this.snapshots;
        if (null == seasonalStats) {
            seasonalStats = new SeasonalStats(this);
        }
        return seasonalStats;
    }

    public List<BeachSnapshot> sortDateAsc() {
/* TODO: Trying to get rid of this method
        Collections.sort(this.snapshots, new Comparator<BeachSnapshot>() {
            public int compare(BeachSnapshot b1, BeachSnapshot b2) {
                return b1.scrapeTime.compareTo(b2.scrapeTime);
            }
        });
*/
        return this.snapshots;
    }
}
