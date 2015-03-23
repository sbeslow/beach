package models;

import beachNinja.SeasonalStats;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Beach extends Model {

    @Id
    public long id;

    public String name;
    public String urlCode;

    @OneToMany(mappedBy = "beach")
    public List<BeachSnapshot> snapshots;

    @Transient
    private SeasonalStats seasonalStats = null;

    public Beach(String[] beachFields) {
        this.name = beachFields[0];
        this.urlCode = beachFields[1];
    }

    public static Finder<Long,Beach> find = new Finder<>(
            Long.class, Beach.class
    );

    public String currentStatus() throws Exception {
        List<BeachSnapshot> snapshots = BeachSnapshot.find.where().eq("beach.id", id).orderBy("scrapeTime desc")
                .findList();
        BeachSnapshot mostRecentSnapshot = snapshots.get(0);
        return mostRecentSnapshot.swimStatus;

    }

    public SeasonalStats getSeasonalStats() {
        if (null == seasonalStats) {
            seasonalStats = new SeasonalStats(this);
        }
        return seasonalStats;
    }

    public List<BeachSnapshot> sortDateAsc() {

        Collections.sort(this.snapshots, new Comparator<BeachSnapshot>() {
            public int compare(BeachSnapshot b1, BeachSnapshot b2) {
                return b1.scrapeTime.compareTo(b2.scrapeTime);
            }
        });

        return this.snapshots;
    }
}
