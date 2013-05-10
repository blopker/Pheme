import pheme.api.Component;
import pheme.api.ComponentType;
import pheme.api.Pheme;

public class Starter {
	static Pheme pheme;

	public static void main(String[] args) {
    // Start Pheme server.
    play.core.server.NettyServer.main(new String[]{"."});

    // Connect to server we just made.
		String hostname = "localhost";
		pheme = new Pheme(hostname);
		Component starter = pheme.register(Starter.class.getSimpleName(), ComponentType.JOB);

		starter.log("info", "I'M ALIVE. SEND HELP. At least there's beer here.");

		starter.count("Beers on the wall", 99);
		for (int i = 99; i > 0; i--) {
			starter.count("Beers on the wall", -1);
			wait(500);
		}

		starter.log("info", "Out of beer! Now we're really in trouble.");
		wait(500);
		System.exit(0);
	}

	private static void wait(int milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
