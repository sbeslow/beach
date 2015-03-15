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

    @Id
    public long id;

    public DateTime scrapeTime;

    @ManyToOne
    public Beach beach;

    public static Finder<Long,BeachSnapshot> find = new Finder<>(
            Long.class, BeachSnapshot.class
    );
}
