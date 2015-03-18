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

    public static final String NO_RESTRICTIONS = "no-restrictions";
    public static final String SWIM_ADVISORY = "swim-advisory";
    public static final String SWIM_BAN = "swim-ban";

    @Id
    public long id;

    public DateTime scrapeTime;

    @ManyToOne
    public Beach beach;

    public String swimStatus = null;

    public static Finder<Long,BeachSnapshot> find = new Finder<>(
            Long.class, BeachSnapshot.class
    );
    public int forecastForToday;
    public int mostRecentResult;
    public String resultCollected;

    public BeachSnapshot(Beach beach) {
        this.beach = beach;
    }
}
