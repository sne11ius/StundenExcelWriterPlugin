package nu.wasis.stunden.plugins.excelwriter.util;

import java.util.List;

public class StringUtils {

	private StringUtils() {
		// static only
	}
	

	public static boolean containsAny(final String projectName, final List<String> indicators) {
		for (final String indicator : indicators) {
			if (projectName.toLowerCase().contains(indicator.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
