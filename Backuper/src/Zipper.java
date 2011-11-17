/***
 * Clase: Zipper.java
 * Fecha: 16/11/2011
 * Autor: silvestre
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class Zipper {

	static final int BUFFER = 2048;
	static final String ZIP_EXTENSION = ".zip";

	/** The Constant logger. */
	private final static Logger logger = org.apache.log4j.Logger
			.getLogger(Zipper.class);

	/**
	 * Creates the zip file.
	 * 
	 * @param folderName
	 *            the folder name
	 * @param output
	 *            the output
	 * @throws Exception
	 *             the exception
	 */
	public static void createZipFile(String folderName, String output)
			throws Exception {
		FileOutputStream fout = new FileOutputStream(output);
		ZipOutputStream zout = new ZipOutputStream(fout);
		logger.info("Creando archivo " + output);
		zout.setComment(PropertiesUtil.getPropertie("zipComment"));
		zout.setLevel(Integer.valueOf(PropertiesUtil.getPropertie("zipLevel")));
		zout.setMethod(ZipOutputStream.DEFLATED);
		zipFile(folderName, zout, File.separator);
		zout.flush();
		zout.close();
	}

	/**
	 * Zip file.
	 * 
	 * @param path
	 *            the path
	 * @param out
	 *            the out
	 * @param relPath
	 *            the rel path
	 * @throws Exception
	 *             the exception
	 */
	private static void zipFile(String path, ZipOutputStream out, String relPath)
			throws Exception {
		File file = new File(path);

		if (!file.exists()) {
			logger.error(file.getName() + " no existe");
			return;
		}
		byte[] buf = new byte[BUFFER];
		String[] files = file.list();

		if (files == null) {
			FileInputStream in = new FileInputStream(file.getAbsolutePath());
			try {
				logger.info("Agregando " + file.getName()
						+ " al archivo comprimido");
				out.putNextEntry(new ZipEntry(relPath + file.getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			} catch (ZipException zipE) {
				String msg = zipE.getMessage();
				if (msg.startsWith("duplicate entry")) {
					logger.error(file.getName() + " ya existe!");
				} else {
					throw zipE;
				}
			} finally {
				if (out != null)
					out.closeEntry();
				if (in != null)
					in.close();
			}
		} else if (files.length > 0) {
			for (int i = 0; i < files.length; ++i) {
				zipFile(path + File.separator + files[i], out,
						relPath + file.getName() + File.separator);
			}
		}
	}

}
