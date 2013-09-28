package nu.wasis.stunden.plugins.excelwriter.util;

import nu.wasis.stunden.model.Entry;

public class TravelDetectionResult {

	private final Entry arrival;
	private final Entry departure;
	
	private final boolean travelDetected;

	public TravelDetectionResult(final Entry arrival, final Entry departure, final boolean travelDetected) {
		this.arrival = arrival;
		this.departure = departure;
		this.travelDetected = travelDetected;
	}

	public Entry getArrival() {
		return arrival;
	}

	public Entry getDeparture() {
		return departure;
	}

	public boolean isTravelDetected() {
		return travelDetected;
	}
	
}
