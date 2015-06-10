package scoreboard;

import models.BeachSnapshot;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.Logger;

public class EcoliMeasurement implements Comparable<EcoliMeasurement> {

    private static final Logger.ALogger logger = Logger.of(EcoliMeasurement.class);
	
	private String date;
	
	private Double reading;
	private Double prediction;
	
	private String maxSwimStatus;

	public EcoliMeasurement(LocalDate date) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
        this.date = fmt.print(date);

		this.maxSwimStatus = BeachSnapshot.NO_RESTRICTIONS;
		reading = 0.0;
		prediction = 0.0;
	}
	
	public LocalDate measDate() {
        try {
            String[] dateSep = date.split("/");
            return new LocalDate(
                Integer.parseInt(dateSep[2]),
                Integer.parseInt(dateSep[0]),
                Integer.parseInt(dateSep[1])
            );
        }
        catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
	}

	public Double getReading() {
		return reading;
	}
	public Double getPrediction() {
		return prediction;
	}

	public void setReading(Double reading) {
		if (reading != null)
			this.reading = reading;
	}

	public void setPrediction(Double prediction) {
		if (prediction != null)
			this.prediction = prediction;
	}
	
	public String printDate() {
        return date;
	}

    public String getDate() {
        return date;
    }
	
	public void setMaxSwimStatus(String maxSwimStatus) {
		this.maxSwimStatus = maxSwimStatus;
	}
	
	public String getMaxSwimStatus() {
		return maxSwimStatus;
	}

	@SuppressWarnings("NullableProblems")
    @Override
	public int compareTo(EcoliMeasurement o) {
		if (this.measDate().isBefore(o.measDate())) {
			return -1;
		}
		else if (this.measDate().isAfter(o.measDate()))
			return 1;
		return 0;
	}
	
}
