import pheme.api.Component;
import pheme.api.ComponentType;
import pheme.api.Pheme;

public class Starter {
	static Pheme pheme;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = System.getProperty("user.dir");
		System.out.println(dir);
		String hostname = getHostname(args);
		pheme = new Pheme(hostname);
		Component starter = pheme.register(Starter.class.getSimpleName(), ComponentType.JOB);
		
		starter.log("info", "I'M ALIVE. SEND HELP. At least there's beer here.");
		
		starter.count("Beers on the wall", 99);
		for (int i = 99; i > 1; i--) {
			starter.count("Beers on the wall", -1);
			wait(500);
		}
		
		starter.log("info", "Out of beer! Now we're really in trouble.");
		wait(500);
		System.exit(0);
	}

	
	private static String getHostname(String[] args) {
		String hostname = "localhost";
		if (args.length >= 1) {
			hostname = args[0];
		}
		return hostname;
	}
	
	private static void wait(int milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
