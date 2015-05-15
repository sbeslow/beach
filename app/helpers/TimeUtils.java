package helpers;

import org.joda.time.DateTime;

public abstract class TimeUtils {

    public static Integer minutesDifference(DateTime earlierTime, DateTime laterTime) {
        long msDiff = laterTime.getMillis() - earlierTime.getMillis();
        return ((int) (msDiff / 3600000));
    }
}
