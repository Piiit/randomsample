package log;

public class Log {
	
	public static void debug(final String output) {
		String debug = System.getenv("ADMT_DEBUG");
		if(debug != null && debug.equalsIgnoreCase("true")) {
			print(output, LogType.DEBUG);
		}
	}
	
	public static void info(final String output) {
		print(output, LogType.INFORMATION);
	}
	
	public static void warn(final String output) {
		print(output, LogType.WARNING);
	}
	
	public static void error(final String output) {
		print(output, LogType.ERROR);
	}
	
	private static void print(String output, LogType type) {
		output = type.toString() + ": " + output;
		if (type == LogType.ERROR) {
			System.err.println(output);
		} else {
			System.out.println(output);
		}
	}
}
