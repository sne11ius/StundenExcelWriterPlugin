package nu.wasis.stunden.plugins.excelwriter.util;

import java.util.List;

import nu.wasis.stunden.model.Day;
import nu.wasis.stunden.model.Entry;

import org.apache.log4j.Logger;

public class TravelDetector {

	private static final Logger LOG = Logger.getLogger(TravelDetector.class);
	
	private final List<String> arrivalIndicators;
	private final List<String> departureIndicators;

	public TravelDetector(final List<String> arrivalIndicators, final List<String> departureIndicators) {
		this.arrivalIndicators = arrivalIndicators;
		this.departureIndicators = departureIndicators;
	}
	
	public TravelDetectionResult detectTravel(final Day day) {
		Entry arrival = null;
		Entry departure = null;
		for (final Entry entry : day.getEntries()) {
			final String projectName = entry.getProject().getName();
			if (StringUtils.containsAny(projectName, arrivalIndicators)) {
				arrival = entry;
			} else if (StringUtils.containsAny(projectName, departureIndicators)) {
				departure = entry;
			}
			if (null != arrival && null != departure) {
				break;
			}
		}
		final boolean travelDetected = null != arrival && null != departure;
		if (null == arrival && null != departure) {
			LOG.warn("no arrival, but departure: " + day);
		}
		if (null != arrival && null == departure) {
			LOG.warn("arrival, but no departure: " + day);
		}
		return new TravelDetectionResult(arrival, departure, travelDetected);
	}

	public List<String> getArrivalIndicator() {
		return arrivalIndicators;
	}

	public List<String> getDepartureIndicator() {
		return departureIndicators;
	}
	
}
