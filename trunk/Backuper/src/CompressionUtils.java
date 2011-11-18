/***
 * Clase: CompressionUtils.java
 * Fecha: 17/11/2011
 * Autor: silvestre
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;

public class CompressionUtils {

	static final int BUFFER = 2048;
	static final String ZIP_EXTENSION = ".zip";
	static final String ZIP_FORMAT = "zip";
	static final String TARGZ_EXTENSION = ".tar.gz";
	static final String TARGZ_FORMAT = "tar";

	private final static Logger logger = org.apache.log4j.Logger
			.getLogger(CompressionUtils.class);

	/**
	 * Creates the zip file.
	 * 
	 * @param directoryPath
	 *            the directory path
	 * @param output
	 *            the output
	 * @throws Exception
	 *             the exception
	 */
	public static void createZipFile(String directoryPath, String output)
			throws Exception {
		FileOutputStream fout = new FileOutputStream(output);
		ZipOutputStream zout = new ZipOutputStream(fout);
		logger.info("Creando archivo " + output);
		zout.setMethod(ZipOutputStream.DEFLATED);
		addToZipFile(directoryPath, zout, File.separator);
		zout.flush();
		zout.close();
	}

	/**
	 * Adds the to zip file.
	 * 
	 * @param path
	 *            the path
	 * @param zipPath
	 *            the zip path
	 * @param relPath
	 *            the rel path
	 * @throws Exception
	 *             the exception
	 */
	private static void addToZipFile(String path, ZipOutputStream zipPath,
			String relPath) throws Exception {
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
				logger.info("Agregando " + file.getName());
				zipPath.putNextEntry(new ZipEntry(relPath + file.getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					zipPath.write(buf, 0, len);
				}
				zipPath.closeEntry();
				in.close();
			} catch (ZipException zipE) {
				String msg = zipE.getMessage();
				if (msg.startsWith("duplicate entry")) {
					logger.error(file.getName() + " ya existe!");
				} else {
					throw zipE;
				}
			} finally {
				if (zipPath != null)
					zipPath.closeEntry();
				if (in != null)
					in.close();
			}
		} else if (files.length > 0) {
			for (int i = 0; i < files.length; ++i) {
				addToZipFile(path + File.separator + files[i], zipPath, relPath
						+ file.getName() + File.separator);
			}
		}
	}

	/**
	 * Creates the tar gz file.
	 * 
	 * @param directoryPath
	 *            the directory path
	 * @param tarGzPath
	 *            the tar gz path
	 * @throws Exception
	 *             the exception
	 */
	public static void createTarGzFile(String directoryPath, String tarGzPath)
			throws Exception {
		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		GzipCompressorOutputStream gzOut = null;
		TarArchiveOutputStream tOut = null;

		try {
			fOut = new FileOutputStream(new File(tarGzPath));
			bOut = new BufferedOutputStream(fOut);
			gzOut = new GzipCompressorOutputStream(bOut);
			tOut = new TarArchiveOutputStream(gzOut);

			addFileToTarGz(tOut, directoryPath, "");
		} finally {
			tOut.finish();

			tOut.close();
			gzOut.close();
			bOut.close();
			fOut.close();
		}
	}

	/**
	 * Adds the file to tar gz.
	 * 
	 * @param tOut
	 *            the t out
	 * @param path
	 *            the path
	 * @param base
	 *            the base
	 * @throws Exception
	 *             the exception
	 */
	private static void addFileToTarGz(TarArchiveOutputStream tOut,
			String path, String base) throws Exception {
		File f = new File(path);
		String entryName = base + f.getName();
		TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);

		tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
		tOut.putArchiveEntry(tarEntry);

		if (f.isFile()) {
			IOUtils.copy(new FileInputStream(f), tOut);

			tOut.closeArchiveEntry();
		} else {
			tOut.closeArchiveEntry();

			File[] children = f.listFiles();

			if (children != null) {
				for (File child : children) {
					logger.info("Agregando " + child.getName());
					addFileToTarGz(tOut, child.getAbsolutePath(), entryName
							+ File.separator);
				}
			}
		}
	}
}
