package base;

public class SessionIdManager {
	private static int currentSessionId = 0;

	public synchronized static int incrementAndGetSessionId() {
		return currentSessionId++;
	}

	public synchronized static String getCurrentSessionId() {

		return String.valueOf(currentSessionId);
	}

	public static void main(String[] args) {
		String transType = "14";

		switch (transType) {
		case "12":
			System.out.println("TransType is : 12 ");
			break;
		case "13":
			System.out.println("TransType is : 13 ");
			break;

		case "14":
			System.out.println("TransType is : 14 ");
			break ;
		case "15":
			System.out.println("TransType is : 15 ");
			break;
		case "16":
			System.out.println("TransType is : 16 ");
			break;
		default:
			System.out.println("TransType is not founded.....");
		}
		
		System.out.println("Loop Is done ");
	}
}
