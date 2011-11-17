/***
 * Clase: Main.java
 * Fecha: 16/11/2011
 * Autor: silvestre
 */
import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

public class Main {

	private final static Logger logger = org.apache.log4j.Logger
			.getLogger(Main.class);

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		List<String> inputFolders = FileReader.getInputFolders();
		File backupDir = new File(PropertiesUtil.getPropertie("backupFolder")
				+ File.separator + DateUtils.getCurrentDate());
		try {
			if (backupDir.exists()) {
				logger.info("El directorio del back ya existe");
				logger.info("Borrando directorio");
				if (backupDir.delete()) {
					logger.info("El directorio se borro con exito");
				} else {
					logger.error("El directorio no se pudo borrar");
				}
			}
			if (backupDir.mkdir()) {
				logger.info("Se creo el directorio del backup: "
						+ backupDir.getName());
			} else {
				logger.error("No se pudo crear el directorio del backup: "
						+ backupDir.getName());
			}
		} catch (Exception e) {
			logger.error("Error creando directorio del backup: "
					+ e.getMessage());
		}
		logger.info("Iniciando backup de carpetas");
		for (String folder : inputFolders) {
			File input = new File(folder);
			logger.info("Carpeta: " + input.getName());
			String output = backupDir + File.separator + input.getName()
					+ Zipper.ZIP_EXTENSION;
			try {
				Zipper.createZipFile(folder, output);
			} catch (Exception e) {
				logger.error("Error comprimiendo carpeta: " + folder + " : "
						+ e.getMessage());
			}
		}
	}
}
