package models;

import beachNinja.SeasonalStats;
import play.db.ebean.Model;

import javax.persistence.*;
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

    public SeasonalStats getSeasonalStats() {
        if (null == seasonalStats) {
            seasonalStats = new SeasonalStats(this);
        }
        return seasonalStats;
    }

    /* TODO: Trying to get rid of this method
    public List<BeachSnapshot> sortDateAsc() {

        Collections.sort(this.snapshots, new Comparator<BeachSnapshot>() {
            public int compare(BeachSnapshot b1, BeachSnapshot b2) {
                return b1.scrapeTime.compareTo(b2.scrapeTime);
            }
        });

        return this.snapshots;
    }
    */
}
