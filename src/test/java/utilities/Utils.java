package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Utils {

	static String amount;
	private static String configFile = "config.properties";


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

	public static String setFileName(String fileName) {
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		String finalDate = dateFormatter.format(now);
		String FILE_NAME = finalDate + "_" + fileName + ".xlsx";

		return FILE_NAME;

	}

	public static String getEnvironment() {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(configFile)) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		String env = properties.getProperty("ENV", "UAT");
		return env;
	}

	public static String getCashBackValue() {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(configFile)) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		String CashBackValue = properties.getProperty("CashBackFlag", "3");
		return CashBackValue;
	}

	public static String getShowResponseValue() {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(configFile)) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		String showResponse = properties.getProperty("ShowScreen", "0");
		return showResponse;
	}

	public static String getSessionId() {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(configFile)) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		String SessionId = properties.getProperty("SessionID", "00");
		return SessionId;
	}

	public static List<String> selectToken(List<String> tokens) {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(configFile)) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		String SaleType = properties.getProperty("TokenType");
		if (SaleType.equalsIgnoreCase("CRMToken")) {

			tokens.set(1, null);
			tokens.set(2, null);
		} else if (SaleType.equalsIgnoreCase("CardIdentifier")) {
			tokens.set(1, null);
			tokens.set(3, null);
		} else if (SaleType.equalsIgnoreCase("CardToken")) {
			tokens.set(3, null);
			tokens.set(2, null);
		}

		return tokens;

	}

}
