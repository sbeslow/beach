package models;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SignificantError extends Model {

	private static final long serialVersionUID = 1L;

	@Id
    public long id;

    private DateTime dateTime = new DateTime();

    private String message;

    private SignificantError(String message) {
        if (message.length() > 255) {
        	message = message.substring(0, 255);
        }
        this.message = message;
    }

    public static void write(String[] rootCauseStackTrace) {
        StringBuilder sb = new StringBuilder();

        for (String stackTrace: rootCauseStackTrace) {
            sb.append(stackTrace + "\n");
        }

        (new SignificantError(sb.toString())).save();
    }

    public static void write(Exception e) {
        StringBuilder sb = new StringBuilder();

        for (String stackTrace: ExceptionUtils.getRootCauseStackTrace(e)) {
            sb.append(stackTrace + "\n");
        }

        (new SignificantError(sb.toString())).save();
    }


}
