package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.testng.Assert;

public class Utils {

	private static final String CONFIG_FILE = "config.properties";

	// Static variables to store configuration values
	private static String environment;
	private static String TokenType;

	private static String cashBackValue;
	private static String showResponseValue;
	private static String sessionId;
	private static String serverIp;
	private static int serverPort;
	private static String ATVTender;
	private static String PrintData;

	static {
		// Load the configuration once when the class is loaded
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
			properties.load(input);
			environment = properties.getProperty("ENV", "Prod");
			TokenType = properties.getProperty("TokenType", "CardToken");
			cashBackValue = properties.getProperty("CashBackFlag", "3");
			showResponseValue = properties.getProperty("ShowScreen", "0");
			sessionId = properties.getProperty("SessionID", "00");
			serverIp = properties.getProperty("ServerIP", getHostIP());
			ATVTender = properties.getProperty("ATV_Token_Tenders", "");
			PrintData = properties.getProperty("Print_Data", "Y");
			serverPort = Integer.parseInt(properties.getProperty("ServerPort", "8060"));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println(
					"The server port number in the configuration is invalid, so we are setting the default port to 8060.");
			serverPort = 8060; // Default port number if parsing fails

		}
	}

	// Static methods to access configuration values
	public static String getEnvironment() {
		return environment;
	}

	public static String getTokenType() {
		return TokenType;
	}

	public static String getCashBackValue() {
		return cashBackValue;
	}

	public static String getShowResponseValue() {
		return showResponseValue;
	}

	public static String getSessionId() {
		return sessionId;
	}

	public static String getServerIp() {
		return serverIp;
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static String getATVTender() {
		return ATVTender;
	}

	public static String getPrintDataParameter() {
		return PrintData;
	}

	public static List<String> generateDateTimeAndInvoice() {
		Date now = new Date();
		SimpleDateFormat invoiceDateFormatter = new SimpleDateFormat("MMddyy");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MMddyyyy");
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");

		String formattedDate = invoiceDateFormatter.format(now);
		String formattedTime = timeFormatter.format(now);
		String finalDate = dateFormatter.format(now);

		String invoiceNumber = formattedDate + "23450087" + formattedTime.substring(0, 5);

		List<String> result = new ArrayList<>();
		result.add(formattedTime);
		result.add(finalDate);
		result.add(invoiceNumber);

		return result;
	}

	private static String getHostIP() {
		try {
			InetAddress localhost = InetAddress.getLocalHost();

			// System.out.println("-".repeat(80) + localhost.getHostName() +
			// "-".repeat(80));
			return localhost.getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null; // Default fallback IP
		}
	}

	public static String setFileName(String fileName) {
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		String finalDate = dateFormatter.format(now);
		return finalDate + "_" + fileName + ".xlsx";
	}

	public static List<String> selectToken(List<String> tokens, String CardType) {
		if (CardType != null && !CardType.isEmpty()) {
			switch (CardType) {
			case "EBC":
			case "EBF":
			case "EBW":
			case "GCC":
			case "EPP":
				tokens.set(3, null);
				tokens.set(2, null);
				return tokens;
			default:
				// Handle other CardTypes or ignore
				break;
			}
		}

		String TokenType = getTokenType(); // Assuming "ENV" is used for sale type
		if ("CRMToken".equalsIgnoreCase(TokenType)) {
			tokens.set(1, null);
			tokens.set(2, null);
		} else if ("CardIdentifier".equalsIgnoreCase(TokenType)) {
			tokens.set(1, null);
			tokens.set(3, null);
		} else if ("CardToken".equalsIgnoreCase(TokenType)) {
			tokens.set(3, null);
			tokens.set(2, null);
		}

		return tokens;
	}

	public static void printResults(List<String> data) {

		if (getPrintDataParameter().equalsIgnoreCase("Y")) {

			System.out.println("Transaction ID  :  " + data.get(11));
			System.out.println("Response Text  :  " + data.get(9));
			System.out.println("Response Code  :  " + data.get(10));
			System.out.println("CardType : " + data.get(6));
			System.out.println("CardEntryMode : " + data.get(3));
		}
	}

	public static void checkEligibleTender(String cardType) {
		String upperCaseTender = ATVTender.toUpperCase();

		switch (upperCaseTender) {
		case "PLC":
		case "XXC":
			Assert.assertEquals(cardType, upperCaseTender);
			break;
		default:
			// Optionally, you can handle unexpected tender types here
			break;
		}
	}

}
