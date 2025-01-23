package base;

public class SessionIdManager {
	private static int currentSessionId = 01;

	public synchronized static int incrementAndGetSessionId() {
		return currentSessionId++;
	}

	public synchronized static String getCurrentSessionId() {

		return String.valueOf(currentSessionId);
	}

	public static void main(String[] args) throws Exception {
		

		
	}
}
