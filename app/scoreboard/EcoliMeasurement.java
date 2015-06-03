package scoreboard;

import org.joda.time.LocalDate;

public class EcoliMeasurement {
	
	private LocalDate date;
	
	private Double reading;
	private Double prediction;
	
	public EcoliMeasurement(LocalDate date) {
		this.date = date;
	}
	
	public LocalDate date() {
		return date;
	}

	public Double getReading() {
		return reading;
	}
	public Double getPrediction() {
		return prediction;
	}

	public void setReading(Double reading) {
		this.reading = reading;
	}

	public void setPrediction(Double prediction) {
		this.prediction = prediction;
	}
	
}
