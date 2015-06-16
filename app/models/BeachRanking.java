package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;
import scoreboard.EcoliMeasurement;

public class BeachRanking {

    private static final Logger.ALogger logger = Logger.of(BeachRanking.class);

    public Integer rank;
    public String beachName;
    public String beachUrl;
    public Double lat;
    public Double lon;

    public double poopScore;
    public String currentStatus;
    public int hoursRecorded;
    public String lastUpdated;

    public List<EcoliMeasurement> ecoliMeasurements;

    public BeachRanking(Beach beach) {
        this.beachName = beach.name;
        this.beachUrl = beach.urlCode;
        this.lat = beach.latitude;
        this.lon = beach.longitude;
        
        buildPoopReadings(beach);
    }
    
    private void buildPoopReadings(Beach beach) {
        poopScore = 0;

        // Humboldt Park Beach is closed for the summer
        if (beach.urlCode.equals("Humboldt-Beach")) {
            ecoliMeasurements = new ArrayList<>(1);

            poopScore = 0;
            currentStatus = "Closed";

            DateTime lastUpdatedDt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM dd yyyy @ hh:mm aa");
            lastUpdated = fmt.print(lastUpdatedDt);

            hoursRecorded = 0;
            return;
        }
        
        int minsNoRestrict = 0;
        int minsAdvisory = 0;
        int minsSwimBan = 0;
        
        Map<LocalDate, EcoliMeasurement> ecoliMap = new HashMap<>();
        BeachSnapshot lastSnapshot = null;

        for (BeachSnapshot thisSnapshot : beach.snapshots) {

            if (thisSnapshot.resultDate == null) {
                try {
                    String dateStr = thisSnapshot.resultCollected.replace("sample collected on ", "");
                    dateStr = dateStr.replace("<small><em>", "");
                    dateStr = dateStr.replace("</em></small>", "");
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM dd, yyyy");
                    thisSnapshot.resultDate = LocalDate.parse(dateStr, fmt);
                    thisSnapshot.update();
                }
                catch (Exception e) {
                    logger.error("Unable to fill result date for snapshot: " + thisSnapshot.id);
                    continue;
                }
            }

        	EcoliMeasurement ecoliMeas = ecoliMap.get(thisSnapshot.scrapeTime.toLocalDate());
        	if (ecoliMeas == null) {
        		ecoliMeas = new EcoliMeasurement(thisSnapshot.scrapeTime.toLocalDate());
        	}
        	ecoliMeas.setPrediction(thisSnapshot.forecastForToday);
        	if (thisSnapshot.swimStatus.equals(BeachSnapshot.SWIM_ADVISORY)) {
        		if (ecoliMeas.getMaxSwimStatus().equals(BeachSnapshot.NO_RESTRICTIONS))
        			ecoliMeas.setMaxSwimStatus(BeachSnapshot.SWIM_ADVISORY);
        	}
        	else if (thisSnapshot.swimStatus.equals(BeachSnapshot.SWIM_BAN)) {
        		ecoliMeas.setMaxSwimStatus(BeachSnapshot.SWIM_BAN);
        	}
        	
        	if (ecoliMeas.measDate().getYear() == 2015)
        		ecoliMap.put(ecoliMeas.measDate(), ecoliMeas);

        	ecoliMeas = ecoliMap.get(thisSnapshot.resultDate);
        	if (ecoliMeas == null) {
        		ecoliMeas = new EcoliMeasurement(thisSnapshot.resultDate);
        	}
        	ecoliMeas.setReading(thisSnapshot.mostRecentResult);
        	
        	if (ecoliMeas.measDate().getYear() == 2015)
        		ecoliMap.put(ecoliMeas.measDate(), ecoliMeas);
       	

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
        
        ecoliMeasurements = new ArrayList<>(ecoliMap.values());
        Collections.sort(ecoliMeasurements);

        poopScore = (minsAdvisory/60.0) + (2.0 * (minsSwimBan/60.0));
        currentStatus = beach.snapshots.get(beach.snapshots.size() - 1).swimStatus;
        
        DateTime lastUpdatedDt = beach.snapshots.get(beach.snapshots.size() - 1).scrapeTime;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM dd yyyy @ hh:mm aa");
        lastUpdated = fmt.print(lastUpdatedDt);

        hoursRecorded = minsAdvisory + minsSwimBan + minsNoRestrict;
    	
    }
    
    public String currentStatusColor() {
        switch (currentStatus) {
            case BeachSnapshot.NO_RESTRICTIONS:
                return "green";
            case BeachSnapshot.SWIM_ADVISORY:
                return "orange";
            case BeachSnapshot.SWIM_BAN:
                return "red";
        }
    	return "gray";
    }
    
    public String printPoopScore() {
    	return String.format("%.2f", poopScore);
    }

    public String rankColor() {
        if (rank <= 5)
            return "red";
        else if (rank <= 10)
            return "orange";
        else
            return "green";
    }
}
