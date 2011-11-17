/***
 * Clase: DateUtils.java
 * Fecha: 16/11/2011
 * Autor: silvestre
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * The Class DateUtils.
 */
public class DateUtils {
	private final static Logger logger = org.apache.log4j.Logger
			.getLogger(DateUtils.class);

	/**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
	public static String getCurrentDate() {
		String ret = "";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			ret = sdf.format(calendar.getTimeInMillis());
		} catch (Exception e) {
			logger.error("Error obteniendo fecha actual");
		}
		return ret;
	}
}
