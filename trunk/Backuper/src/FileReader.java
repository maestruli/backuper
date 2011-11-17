/***
 * Clase: FileReader.java
 * Fecha: 16/11/2011
 * Autor: silvestre
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * The Class FileReader.
 */
public class FileReader {

	private final static Logger logger = org.apache.log4j.Logger
			.getLogger(FileReader.class);

	public static List<String> inputFolders = new ArrayList<String>();

	public static String FOLDERLIST_FILE = "config/folder.list";
	static {
		try {
			FileInputStream inputFile = new FileInputStream(FOLDERLIST_FILE);
			DataInputStream dis = new DataInputStream(inputFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));

			String temp = "";

			while ((temp = br.readLine()) != null) {
				inputFolders.add(temp);
			}
			inputFile.close();
		} catch (Exception e) {
			logger.error("Error leyendo carpetas a backapear: "
					+ e.getMessage());
		}
	}

	/**
	 * Gets the input folders.
	 * 
	 * @return the input folders
	 */
	public static List<String> getInputFolders() {
		return inputFolders;
	}

}
