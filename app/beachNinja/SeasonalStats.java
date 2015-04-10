package beachNinja;

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

    public Integer score() {
        int score = minsAdvisory;
        return score + (2 * minsSwimBan);
    }

    public int percentNoRestrict() {
        int totalMins = minsAdvisory + minsSwimBan + minsNoRestrict;
        double per = 100.0 * minsNoRestrict/totalMins;
        return (int) Math.round(per);
    }

    public int percentAdvisory() {
        int totalMins = minsAdvisory + minsSwimBan + minsNoRestrict;
        double per = 100.0 * minsAdvisory/totalMins;
        return (int) Math.round(per);
    }

    public int percentBan() {
        int totalMins = minsAdvisory + minsSwimBan + minsNoRestrict;
        double per = 100.0 * minsSwimBan/totalMins;
        return (int) Math.round(per);
    }
}
