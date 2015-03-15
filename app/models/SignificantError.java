package models;

import org.joda.time.DateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SignificantError extends Model {

    @Id
    public long id;

    private DateTime dateTime = new DateTime();

    private String message;

    private SignificantError(String message) {
        this.message = message;
    }

    public static void write(String[] rootCauseStackTrace) {
        StringBuilder sb = new StringBuilder();

        for (String stackTrace: rootCauseStackTrace) {
            sb.append(stackTrace + "\n");
        }

        (new SignificantError(sb.toString())).save();
    }


}
