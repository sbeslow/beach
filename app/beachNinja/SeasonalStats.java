package beachNinja;

import models.Beach;
import models.BeachSnapshot;

import java.io.Serializable;

public class SeasonalStats implements Serializable {

    private int hoursNoRestrict = 0;
    private int hoursAdvisory = 0;
    private int hoursSwimBan = 0;

    public int getHoursNoRestrict() {
        return hoursNoRestrict;
    }

    public int getHoursAdvisory() {
        return hoursAdvisory;
    }

    public int getHoursSwimBan() {
        return hoursSwimBan;
    }

    public SeasonalStats(Beach beach) {

        BeachSnapshot lastSnapshot = null;

        for (BeachSnapshot thisSnapshot : beach.sortDateAsc()) {

            // If this is the first pass or if this is a new day.  Reset lastSnapshot and move to the next one
            if ((lastSnapshot == null) || (!lastSnapshot.scrapeTime.toLocalDate().equals(thisSnapshot.scrapeTime.toLocalDate()))) {
                lastSnapshot = thisSnapshot;
                continue;
            }

            long msPassed = thisSnapshot.scrapeTime.getMillis() - lastSnapshot.scrapeTime.getMillis();
            int hoursPassed = (int) (msPassed / 3600000);

            switch (thisSnapshot.swimStatus) {
                case BeachSnapshot.NO_RESTRICTIONS:
                    hoursNoRestrict += hoursPassed;
                    break;
                case BeachSnapshot.SWIM_ADVISORY:
                    hoursAdvisory += hoursPassed;
                    break;
                case BeachSnapshot.SWIM_BAN:
                    hoursSwimBan += hoursPassed;
                    break;
            }
        }
    }
}
