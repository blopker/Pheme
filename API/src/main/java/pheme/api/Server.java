package pheme.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;

public class Server {
	private Process serverProc;
	private static String EXTRACT_FOLDER = "/tmp/pheme";

	public static void main(String[] args) {
		Server.startServer();
	}

	protected static Server startServer() {
		return startServer(true);
	}

	protected static Server startServer(boolean autoShutdown) {
		Server server = null;
		try {
			server = new Server(autoShutdown);
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return server;
	}

	private Server(boolean autoShutdown) throws ServerNotActiveException,
			IOException {
		String zipLocation = download(
				"http://pheme.in/downloads/pheme-latest.zip", "/tmp");
		if (zipLocation == null) {
			System.out.println("Download failed, aborting.");
			throw new ServerNotActiveException();
		}

		UnzipUtil.unzipMyZip(zipLocation, EXTRACT_FOLDER);

		String path = getNewestFolderName(EXTRACT_FOLDER);
		System.out.println("Running: " + "bash " + path + "start");
		String[] cmd = { "bash", path + "start" };
		serverProc = new ProcessBuilder(cmd).redirectError(Redirect.INHERIT)
				.redirectOutput(Redirect.INHERIT).start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (autoShutdown) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					kill();
				}
			});

		}
	}

	private String getNewestFolderName(String folderpath) {
		File[] files = new File(EXTRACT_FOLDER).listFiles();
		File newest = (files.length > 0) ? files[0] : null;
		if (newest == null) {
			return "";
		}
		for (File file : files) {
			if (file.isDirectory()) {
				if (file.lastModified() > newest.lastModified()) {
					newest = file;
				}
			}
		}
		return newest.getAbsolutePath() + File.separator;
	}

	public void kill() {
		if (serverProc != null) {
			serverProc.destroy();
			System.out.println("Pheme server killed.");
		}
	}

	private String download(String urlLocation, String folderLocation) {
		String[] split = urlLocation.split("/");
		String fileName = split[split.length - 1];
		String fileLocation = folderLocation
				+ System.getProperty("file.separator") + fileName;

		if (new File(fileLocation).exists()) {
			System.out.println("Server already downloaded, continue.");
			return fileLocation;
		}

		try {
			/*
			 * Get a connection to the URL and start up a buffered reader.
			 */
			long startTime = System.currentTimeMillis();

			System.out.println("Connecting to Pheme site...\n");

			URL url = new URL(urlLocation);
			url.openConnection();
			InputStream reader = url.openStream();

			/*
			 * Setup a buffered file writer to write out what we read from the
			 * website.
			 */

			FileOutputStream writer = new FileOutputStream(fileLocation);
			byte[] buffer = new byte[153600];
			int totalBytesRead = 0;
			int bytesRead = 0;

			System.out.println("Reading ZIP file 150KB blocks at a time.\n");

			while ((bytesRead = reader.read(buffer)) > 0) {
				System.out.print(".");
				writer.write(buffer, 0, bytesRead);
				buffer = new byte[153600];
				totalBytesRead += bytesRead;
			}

			long endTime = System.currentTimeMillis();

			System.out.println("Done. "
					+ (new Integer(totalBytesRead).toString())
					+ " bytes read ("
					+ (new Long(endTime - startTime).toString())
					+ " millseconds).\n");
			writer.close();
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return fileLocation;
	}
}
