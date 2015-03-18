package models;

import org.joda.time.DateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Beach extends Model {

    @Id
    public long id;

    public String name;
    public String urlCode;

    @OneToMany(mappedBy = "beach")
    public List<BeachSnapshot> snapshots;

    public Beach(String[] beachFields) {
        this.name = beachFields[0];
        this.urlCode = beachFields[1];
    }

    public static Finder<Long,Beach> find = new Finder<>(
            Long.class, Beach.class
    );

}
