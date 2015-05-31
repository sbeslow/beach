package models;

import beachNinja.SeasonalStats;
import play.db.ebean.Model;

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

    public String currentStatus() throws Exception {
        List<BeachSnapshot> snapshots = BeachSnapshot.find.where().eq("beach.id", id).orderBy("scrapeTime desc")
                .findList();
        BeachSnapshot mostRecentSnapshot = snapshots.get(0);
        return mostRecentSnapshot.swimStatus;

    }

    public SeasonalStats getSeasonalStats() {
        List<BeachSnapshot> snapshots = this.snapshots;
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

    public double pooScore() {

        BeachSnapshot lastSnapshot = null;

        int minsNoRestrict = 0;
        int minsAdvisory = 0;
        int minsSwimBan = 0;

        for (BeachSnapshot thisSnapshot : sortDateAsc()) {

            // If this is the first pass or if this is a new day.  Reset lastSnapshot and move to the next one
            if ((lastSnapshot == null) || (!lastSnapshot.scrapeTime.toLocalDate().equals(thisSnapshot.scrapeTime.toLocalDate()))) {
                lastSnapshot = thisSnapshot;
                continue;
            }

            long msPassed = thisSnapshot.scrapeTime.getMillis() - lastSnapshot.scrapeTime.getMillis();
            int minsPassed = (int) (msPassed / 60000);

            switch (thisSnapshot.swimStatus) {
                case BeachSnapshot.NO_RESTRICTIONS:
                    minsNoRestrict += minsPassed;
                    break;
                case BeachSnapshot.SWIM_ADVISORY:
                    minsAdvisory += minsPassed;
                    break;
                case BeachSnapshot.SWIM_BAN:
                    minsSwimBan += minsPassed;
                    break;
            }
        }

        double pooScore = minsAdvisory / 480.0;
        pooScore += ((2 * minsSwimBan) / 480.0);

        return pooScore;
    }
}
