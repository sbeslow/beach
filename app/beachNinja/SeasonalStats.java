package beachNinja;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import models.Beach;
import models.BeachSnapshot;

public class SeasonalStats {

    private int minsNoRestrict = 0;
    private int minsAdvisory = 0;
    private int minsSwimBan = 0;

    public int getMinsNoRestrict() {
        return minsNoRestrict;
    }

    public int getMinsAdvisory() {
        return minsAdvisory;
    }

    public int getMinsSwimBan() {
        return minsSwimBan;
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
    }

    public double score() {
        int score = minsAdvisory;
        return (score + (2 * minsSwimBan)) / 600.0;
    }

    public double percentNoRestrict() {
        return 100 -  (percentAdvisory() + percentBan());
    }

    public double percentAdvisory() {
        return 100 * minsAdvisory / (minsAdvisory + minsSwimBan + minsNoRestrict);
    }

    public double percentBan() {
        return 100 * minsSwimBan / (minsAdvisory + minsSwimBan + minsNoRestrict);
    }
    
    public double percentWithRestriction() {
    	double percentWithRest = 100 - percentNoRestrict();
    	return round(percentWithRest, 1);
    }
    
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
