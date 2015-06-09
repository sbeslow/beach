package models;

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
}
