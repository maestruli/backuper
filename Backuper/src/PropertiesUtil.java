/***
 * Clase: PropertiesUtil.java
 * Fecha: 16/11/2011
 * Autor: silvestre
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * The Class PropertiesUtil.
 */
public class PropertiesUtil {

	private final static Logger logger = org.apache.log4j.Logger
			.getLogger(PropertiesUtil.class);

	public static String CONFIG_FILE = "config/config.properties";

	/** The properties. */
	public static Properties properties = new Properties();
	static {
		try {
			logger.info("Intentando abrir el archivo de configuracion...");
			properties.load(new FileInputStream(CONFIG_FILE));
		} catch (IOException e) {
			logger.error("Error abriendo el archivo de configuracion: "
					+ e.getMessage());
		}
	}

	/**
	 * Gets the propertie.
	 * 
	 * @param key
	 *            the key
	 * @return the propertie
	 */
	public static String getPropertie(String key) {
		String ret = properties.getProperty(key);
		if (ret == null || ret.isEmpty()) {
			logger.info("No se encontro la Key: " + key + " en el archivo "
					+ CONFIG_FILE);
		}
		return ret;
	}
}
