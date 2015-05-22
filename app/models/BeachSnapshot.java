package models;

import org.joda.time.DateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by scottbeslow on 3/14/15.
 * This class represents a period in time in which beach data was collected from cpdbeaches.com
 */
@Entity
public class BeachSnapshot extends Model {

    public static final String NO_RESTRICTIONS = "No Restrictions";
    public static final String SWIM_ADVISORY = "Swim Advisory";
    public static final String SWIM_BAN = "Swim Ban";

    @Id
    public long id;

    public DateTime scrapeTime;

    @ManyToOne
    public Beach beach;

    public String swimStatus = null;

    public static Finder<Long,BeachSnapshot> find = new Finder<>(
            Long.class, BeachSnapshot.class
    );
    public Integer forecastForToday;
    public Integer mostRecentResult;
    public String resultCollected;

    public BeachSnapshot(Beach beach) {
        this.beach = beach;
    }

    public BeachSnapshot(DateTime scrapeTime, Beach beach, String swimStatus, int forecastForToday,
                         int mostRecentResult, String resultCollected) {
        this.scrapeTime = scrapeTime;
        this.beach = beach;
        this.swimStatus = swimStatus;
        this.forecastForToday = forecastForToday;
        this.mostRecentResult = mostRecentResult;
        this.resultCollected = resultCollected;
    }
}
