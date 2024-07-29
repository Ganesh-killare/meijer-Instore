package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DateUtilities {
	static String amount;

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
		try (FileInputStream input = new FileInputStream("config.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		String env = properties.getProperty("ENV", "UAT");
		return env;
	}

	public static List<String> selectToken(List<String> tokens) {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream("config.properties")) {
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
