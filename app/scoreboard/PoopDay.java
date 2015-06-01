package scoreboard;

import models.BeachSnapshot;
import org.joda.time.DateTime;

public class PoopDay {

    private DateTime dateTime;
    private String swimStatus;

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getSwimStatus() {
        return swimStatus;
    }

    public PoopDay(DateTime dateTime, String swimStatus) {

        this.dateTime = dateTime;
        this.swimStatus = swimStatus;

    }

    public double updatePoopDayAndScore (String newSwimStatus) {

        if (swimStatus.equals(BeachSnapshot.SWIM_BAN))
            return 0;

        if (newSwimStatus.equals(BeachSnapshot.SWIM_ADVISORY)) {
            if (swimStatus.equals(BeachSnapshot.NO_RESTRICTIONS)) {
                swimStatus = BeachSnapshot.SWIM_ADVISORY;
                return 0.5;
            }
        }
        else if (newSwimStatus.equals(BeachSnapshot.SWIM_BAN)) {

            swimStatus = BeachSnapshot.SWIM_BAN;
            if (swimStatus.equals(BeachSnapshot.NO_RESTRICTIONS))

                return 1;

            return 0.5;
        }

        return 0;
    }



}
