package scoreboard;

import models.BeachSnapshot;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EcoliMeasurement implements Comparable<EcoliMeasurement> {
	
	private LocalDate date;
	
	private Double reading;
	private Double prediction;
	
	private String maxSwimStatus;

	public EcoliMeasurement(LocalDate date) {
		this.date = date;
		this.maxSwimStatus = BeachSnapshot.NO_RESTRICTIONS;
		reading = 0.0;
		prediction = 0.0;
	}
	
	public LocalDate getDate() {
		return date;
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
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd");
        return fmt.print(date);
	}
	
	public void setMaxSwimStatus(String maxSwimStatus) {
		this.maxSwimStatus = maxSwimStatus;
	}
	
	public String getMaxSwimStatus() {
		return maxSwimStatus;
	}

	@Override
	public int compareTo(EcoliMeasurement o) {
		if (this.date.isBefore(o.getDate())) {
			return -1;
		}
		else if (this.date.isAfter(o.getDate()))
			return 1;
		return 0;
	}
	
}
