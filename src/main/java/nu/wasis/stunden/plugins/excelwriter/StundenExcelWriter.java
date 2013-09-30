package nu.wasis.stunden.plugins.excelwriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jxl.read.biff.BiffException;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import nu.wasis.stunden.exception.InvalidConfigurationException;
import nu.wasis.stunden.model.Day;
import nu.wasis.stunden.model.Entry;
import nu.wasis.stunden.model.WorkPeriod;
import nu.wasis.stunden.plugin.OutputPlugin;
import nu.wasis.stunden.plugins.excelwriter.config.StundenExcelWriterConfiguration;
import nu.wasis.stunden.plugins.excelwriter.exception.ExcelException;
import nu.wasis.stunden.plugins.excelwriter.util.TravelDetectionResult;
import nu.wasis.stunden.plugins.excelwriter.util.TravelDetector;
import nu.wasis.stunden.util.DateUtils;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Duration;

@PluginImplementation
public class StundenExcelWriter implements OutputPlugin {

	private static final Logger LOG = Logger.getLogger(StundenExcelWriter.class);
	
	private static final String SHEET_NAME_ZEITNACHWEIS = "Zeitnachweis";

	private TravelDetector travelDetector = null;
	
	@Override
	public void output(final WorkPeriod workPeriod, final Object configuration) {
		if (null == configuration || !(configuration instanceof StundenExcelWriterConfiguration)) {
			throw new InvalidConfigurationException("Configuration null or wrong type. You probably need to fix your configuration file.");
		}
		final StundenExcelWriterConfiguration myConfig = (StundenExcelWriterConfiguration) configuration;
		LOG.debug("Travel detection " + (myConfig.getDoTravelDetection() ? "en" : "dis") + "abled."); 
		travelDetector = new TravelDetector(myConfig.getTravelArrivalIndicators(), myConfig.getTravelDepartureIndicators());
		try {
			fillExcelFile(workPeriod, myConfig);
		} catch (BiffException | IOException e) {
			throw new ExcelException("Cannot do this shit D:", e);
		}
	}

	private void fillExcelFile(final WorkPeriod workPeriod, final StundenExcelWriterConfiguration myConfig) throws BiffException, IOException {
		LOG.debug("Entering all the data...");
		final XSSFWorkbook workbook = new XSSFWorkbook(myConfig.getTemplateExcelFilename());
		final XSSFSheet sheet = workbook.getSheet(SHEET_NAME_ZEITNACHWEIS);
		int rowIndex = myConfig.getStartRow();
		for (final Day day : workPeriod.getDays()) {
			if (day.isTagged(myConfig.getTags())) {
				enterRowData(sheet.getRow(rowIndex), day, myConfig);
				++rowIndex;
			}
		}
		LOG.debug("Writing to file...");
//		workbook.getCreationHelper().createFormulaEvaluator().clearAllCachedResultValues();
//		workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		sheet.setForceFormulaRecalculation(true);
		workbook.write(new FileOutputStream(new File(myConfig.getOutputExcelFilename())));
		LOG.debug("...done.");
	}

	private void enterRowData(final XSSFRow row, final Day day, final StundenExcelWriterConfiguration myConfig) {
		row.getCell(myConfig.getDateCol()).setCellValue(DateUtils.DATE_FORMATTER_SHORT.print(day.getDate()));
		final TravelDetectionResult detectionResult = travelDetector.detectTravel(day);
		if (myConfig.getDoTravelDetection()) {
			if (detectionResult.isTravelDetected()) {
				LOG.debug("Travel detected for " + DateUtils.DATE_FORMATTER.print(day.getDate()));
				row.getCell(myConfig.getStartArrivalCol()).setCellValue(DateUtils.TIME_FORMATTER.print(detectionResult.getArrival().getBegin()));
				row.getCell(myConfig.getEndArrivalCol()).setCellValue(DateUtils.TIME_FORMATTER.print(detectionResult.getArrival().getEnd()));
				row.getCell(myConfig.getStartDepartureCol()).setCellValue(DateUtils.TIME_FORMATTER.print(detectionResult.getDeparture().getBegin()));
				row.getCell(myConfig.getEndDepartureCol()).setCellValue(DateUtils.TIME_FORMATTER.print(detectionResult.getDeparture().getEnd()));
			}
		}
		final StringBuilder descriptionBuilder = new StringBuilder(200);
		Duration totalWorkDuration = new Duration(0);
		Entry firstEntry = null;
		for (final Entry entry : day.getEntries()) {
			if (myConfig.getDoTravelDetection() && detectionResult.isTravelDetected() && (detectionResult.getArrival() == entry || detectionResult.getDeparture() == entry)) {
				// this entry is already covered by travel
				continue;
			}
			if (entry.isTagged(myConfig.getTags())) {
				if (null == firstEntry) {
					firstEntry = entry;
				}
				if (!descriptionBuilder.toString().toLowerCase().contains(entry.getProject().getName().toLowerCase())) {
					descriptionBuilder.append(entry.getProject().getName());
					descriptionBuilder.append(", ");
				}
				totalWorkDuration = totalWorkDuration.plus(entry.getDuration());
			}
		}
		if (null == firstEntry) {
			throw new RuntimeException("No starting entry found. This should ... not happen :D");
		}
		row.getCell(myConfig.getStartWorkCol()).setCellValue(DateUtils.TIME_FORMATTER.print(firstEntry.getBegin()));
		final DateTime endTime = firstEntry.getBegin().plus(totalWorkDuration).plusMinutes(myConfig.getAutoBreakBonus());
		row.getCell(myConfig.getEndWorkCol()).setCellValue(DateUtils.TIME_FORMATTER.print(endTime));
		final String description = descriptionBuilder.toString();
		row.getCell(myConfig.getDescriptionCol()).setCellValue(description.substring(0, description.lastIndexOf(",")));
	}

	@Override
	public Class<StundenExcelWriterConfiguration> getConfigurationClass() {
		return StundenExcelWriterConfiguration.class;
	}

}
