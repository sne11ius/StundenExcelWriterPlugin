package nu.wasis.stunden.plugins.excelwriter.config;

import java.util.List;
import java.util.Set;

public class StundenExcelWriterConfiguration {

	private String templateExcelFilename;
	private String outputExcelFilename;
	private Set<String> tags;
	private List<String> travelArrivalIndicators;
	private List<String> travelDepartureIndicators;
	private int autoBreakBonus;
	private int startRow;
	private int dateCol;
	private int startWorkCol;
	private int endWorkCol;
	private int startArrivalCol;
	private int endArrivalCol;
	private int startDepartureCol;
	private int endDepartureCol;
	private int descriptionCol;

	public String getTemplateExcelFilename() {
		return templateExcelFilename;
	}

	public String getOutputExcelFilename() {
		return outputExcelFilename;
	}
	
	public Set<String> getTags() {
		return tags;
	}

	public List<String> getTravelArrivalIndicators() {
		return travelArrivalIndicators;
	}

	public List<String> getTravelDepartureIndicators() {
		return travelDepartureIndicators;
	}

	public int getAutoBreakBonus() {
		return autoBreakBonus;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getDateCol() {
		return dateCol;
	}

	public int getStartWorkCol() {
		return startWorkCol;
	}

	public int getEndWorkCol() {
		return endWorkCol;
	}

	public int getStartArrivalCol() {
		return startArrivalCol;
	}

	public int getEndArrivalCol() {
		return endArrivalCol;
	}

	public int getStartDepartureCol() {
		return startDepartureCol;
	}

	public int getEndDepartureCol() {
		return endDepartureCol;
	}

	public int getDescriptionCol() {
		return descriptionCol;
	}

}
