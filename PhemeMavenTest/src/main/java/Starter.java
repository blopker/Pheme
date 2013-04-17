import java.io.File;


public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = System.getProperty("user.dir");
		play.core.server.NettyServer.createServer(new File(dir));
	}

}
