package beachNinja;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import models.Beach;
import models.BeachSnapshot;
import org.joda.time.DateTime;
import scoreboard.PoopDay;

public class SeasonalStats {

	private double poopScore;

	private List<PoopDay> poopDays;
	private String currentStatus;

	public SeasonalStats(Beach beach) {

		poopDays = new ArrayList<>();
		poopScore = 0;
		int minsNoRestrict = 0;
		int minsAdvisory = 0;
		int minsSwimBan = 0;
		
		
/*
		PoopDay currentPoopDay = new PoopDay(new DateTime(2015, 5, 21, 6, 0), "");
		for (BeachSnapshot thisSnapshot : beach.sortDateAsc()) {

			long msPassed = thisSnapshot.scrapeTime.getMillis()
					- lastSnapshot.scrapeTime.getMillis();
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

		
		for (BeachSnapshot thisSnapshot : beach.sortDateAsc()) {

			// Check if thisSnapshot is before 11 AM or after 7 PM. If so,
			// report error and skip it.
			if (currentPoopDay.getDateTime().toLocalDate()
					.equals(thisSnapshot.scrapeTime.toLocalDate())) {
				poopScore += currentPoopDay
						.updatePoopDayAndScore(thisSnapshot.swimStatus);
			} else {
				if (!currentPoopDay.getSwimStatus().equals("")) {
					poopDays.add(currentPoopDay);
				}
				currentPoopDay = new PoopDay(thisSnapshot.scrapeTime,
						thisSnapshot.swimStatus);
				if (thisSnapshot.swimStatus.equals(BeachSnapshot.SWIM_ADVISORY))
					poopScore += 0.5;
				else if (thisSnapshot.swimStatus.equals(BeachSnapshot.SWIM_BAN))
					poopScore += 1;
			}
		}
*/
	}

	public double getPoopScore() {
		return poopScore;
	}

	public List<PoopDay> getPoopDays() {
		return poopDays;
	}

	public String getCurrentStatus() {
		return this.currentStatus;
	}
}
