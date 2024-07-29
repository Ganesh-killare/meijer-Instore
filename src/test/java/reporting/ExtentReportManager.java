package reporting;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;



public class ExtentReportManager {
	public static ExtentReports extentReports;

	public static ExtentReports createInstance(String fileName, String reportName, String documentTitle) {
		ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(fileName);
		extentSparkReporter.config().setReportName(reportName);
		extentSparkReporter.config().setDocumentTitle(documentTitle);
		extentSparkReporter.config().setTheme(Theme.DARK);
		extentSparkReporter.config().setEncoding("utf-8");

		ExtentReports extentReports = new ExtentReports();
		extentReports.attachReporter(extentSparkReporter);
		extentReports.setSystemInfo("Application", "AESDK Version 6.14.9");
		extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
		extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
		extentReports.setSystemInfo("Environemnt", "UAT");
		extentReports.setSystemInfo("user", "Ganesh Killare");
		return extentReports;
	}

	public static String getReportWIthTimestamp() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		String repName = "Test-Report-" + timeStamp + ".html";
		return repName;
	}

	public static void logPassedDetails(String log) {
		Setup.extentTest.get().pass(MarkupHelper.createLabel(log, ExtentColor.GREEN));
	}

	public static void logFailureDetails(String log) {
		Setup.extentTest.get().fail(MarkupHelper.createLabel(log, ExtentColor.RED));
	}

	public static void logExceptionDetails(String log) {
		Setup.extentTest.get().fail(log);
	}

	public static void logInfoDetails(String log) {
		Setup.extentTest.get().info(MarkupHelper.createLabel(log, ExtentColor.PINK));
	}
	public static void logInfoCardDetails(String log) {
		Setup.extentTest.get().info(MarkupHelper.createLabel(log, ExtentColor.PURPLE));
	}
	
	public static void logCardDataDetails(String log) {
		Setup.extentTest.get().info(MarkupHelper.createLabel(log, ExtentColor.ORANGE));
	}
	public static void logDetails(String  log) {
		Setup.extentTest.get().info(MarkupHelper.createLabel(log, ExtentColor.WHITE));
	}
	

	public static void logWarnDetails(String log) {
		Setup.extentTest.get().warning(MarkupHelper.createLabel(log, ExtentColor.YELLOW));
	}

	public static void logJSONDetails(String log) {
		Setup.extentTest.get().info(MarkupHelper.createCodeBlock(log, CodeLanguage.JSON));
	}



}
