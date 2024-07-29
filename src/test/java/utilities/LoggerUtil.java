package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoggerUtil {
	private static final Logger logger = LogManager.getLogger(LoggerUtil.class);

	// Initialize appName with a default value
	private static String appName = "DefaultSystemName";

	static {
		// Initialize appName with the system name dynamically
		appName = getSystemName();
		// Set appName as a system property for Log4j2
		System.setProperty("appName", appName);
	}

	public static void logRequest(String request) {

		try {
			request.trim();

			String modifiedRequest = request.replaceAll("\\s+", "");

			logger.info(modifiedRequest);

		} catch (Exception e) { // Handle logging exception gracefully
			System.err.println("Error logging Data : " + e.getMessage());
		}
	}
	
	public static void logResponse(String response) {

		try {
			response.trim();

			String modifiedRequest = response.replaceAll("\\s+", "");

			logger.info(modifiedRequest);

		} catch (Exception e) { // Handle logging exception gracefully
			System.err.println("Error logging Data : " + e.getMessage());
		}
	}

	private static String getSystemName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// Handle exception (e.g., log it or provide default system name)
			logger.warn("Failed to get system name: {}", e.getMessage());
			return "UnknownSystem";
		}
	}
}
