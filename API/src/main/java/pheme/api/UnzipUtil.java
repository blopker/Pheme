package pheme.api;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class UnzipUtil {

	/**
	 * This method
	 * --Reads an input stream
	 * --Writes the value to the output stream
	 * --Uses 1KB buffer.
	 */
	private static final void writeFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	public static void unzipMyZip(String zipFileName,
			String directoryToExtractTo) {
		Enumeration<?> entriesEnum;
		ZipFile zipFile;
		
		if (!directoryToExtractTo.endsWith(File.separator)) {
			directoryToExtractTo += File.separator;
		}
		
		try {
			zipFile = new ZipFile(zipFileName);
			entriesEnum = zipFile.entries();
			
			File directory= new File(directoryToExtractTo);
			
			/**
			 * Check if the directory to extract to exists
			 */
			if(!directory.exists())
			{
				/** 
				 * If not, create a new one.
				 */
				new File(directoryToExtractTo).mkdir();
				System.out.println("...Directory Created -"+directoryToExtractTo);
			}
			while (entriesEnum.hasMoreElements()) {
				try {
					ZipEntry entry = (ZipEntry) entriesEnum.nextElement();

					if (entry.isDirectory()) {
						/** 
						 * Currently not unzipping the directory structure. 
						 * All the files will be unzipped in a Directory
						 *  
						 **/
					} else {

						System.out.println("Extracting file: "
								+ entry.getName());
						/**
						 * The following logic will just extract the file name
						 * and discard the directory
						 */
						String name = entry.getName();
						mkdirs(name, directoryToExtractTo);						
						System.out.println(name);

						writeFile(zipFile.getInputStream(entry),
								new BufferedOutputStream(new FileOutputStream(
										directoryToExtractTo + name)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Some Exception Occurred:");
			ioe.printStackTrace();
			return;
		}
	}
	
	private static void mkdirs(String fileName, String baseName){
		String path = baseName + fileName.substring(0, fileName.lastIndexOf(File.separator));
		new File(path).mkdirs();
	}

}
