package utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProcessorFailourXL {

	public List<String> generateTransactionSteps(String responseCode, int counter) {
		List<String> steps = new ArrayList<>();

		// Expected Steps index 0 to 4
		steps.add("Transaction should get routed to primary, and should get approved as hardcoded amount is not used");
		steps.add("Transaction should get routed to primary, and should get declined with   " + responseCode
				+ "   response and host should rout that transaction to Secondary  and it should get approved. Host will set the Primary failover counter to "
				+ counter);
		steps.add(
				"Transaction should get routed to secondary directly as counter limit is exceeded and should get approved as hardcoded amount is not used");
		steps.add("Transaction should get routed to primary, and should get approved as hardcoded amount is not used");
		steps.add(
				"As we have performed approved transaction before the threshold for failover counter was exceeded, failover counter should get reset to zero.");

		// Additional Transaction Steps index 5 to 11

		steps.add("Counter will not get set here as transaction is performed with normal amount");
		steps.add("Counter " + counter + " : Transaction declined  count " + counter + " set");
		steps.add("Here, we will perform basic approval sale transactions to check if the Secoundary is up.");
		steps.add(
				"Performed void transactions for the transactions performed through Worldpay and chase processor, all the void transactions should get routed to their respective processor where parent transaction was approved");
		steps.add(
				"Here, as we have reset the counter, all the transactions should start getting routed to Primary processor again. Transactions should get approved from chase as we have not used hardcoded amounts.");

		steps.add(
				"As we have performed 4 failover and our threshold for failover is 5 counters, we will now perform the approval transaction to check if the counter that is set for above failovers is getting reset to zero again.");

		// for the secoundery processor Index 12

		steps.add("Failover should happened and transaction will get routed to Secoundary processor for approval");

		return steps;
	}

	public List<String> headlineSetter(String Response, String responseText, String primaryProcesssor) {

		switch (Response) {
		case "228":
			responseText = "PROCESSOR NOT REACHABLE";
			break;
		case "512":
			responseText = "PLEASE TRY AGAIN 512";
			break;
		case "597":
			responseText = "NO RESPONSE FROM PROCESSOR";
			break;
		case "91":
			responseText = "Issuer Inoperative";
			break;
		case "92":
			responseText = "Unable to Route Transaction";
			break;
		case "40":
			responseText = "Function Not Available";
			break;
		case "96":
		case "97":
			responseText = "System Error";
			break;
		case "NO":
			responseText = " Issuer or Switch Inoperative (Visa) ";
			break;
		case "99":
			responseText = "Unable to send transaction to be authorized by issuer.";
			break;
		case "56":
			responseText = "Cannot Process";
			break;
		case "05":
			responseText = "Authorizer Not Available (time-out)";
			break;

		default:
			break;
		}

		List<String> headlines = new ArrayList<>();
		headlines.add("Transaction performed after the counter reset");
		headlines.add("Dual processor testing chase and WorldPay (where " + primaryProcesssor
				+ "  is primary and processor response code tested is " + Response + " response text is  "
				+ responseText);
		headlines.add(
				"Failover is now done and all the transactions going forward will get route to Secondary processor directly for the particular tender is done processor failure ");
		headlines.add("Reset the Failover counter to 0 and perform basic sale transactions without hardcoded amount.");
		headlines.add(
				"To check If counter is getting reset to zero, when performed failover 4 time and before the failover threshold approved transaction is done");
		return headlines;
	}

	// EXCEL OPERATION ARE HERE

	Workbook workbook;
	Sheet sheet;

	List<String> parameters;

	public ProcessorFailourXL() {

		parameters = Arrays.asList("Expected Steps ", "CardToken", "CardIdentifier", "CRMToken", "TransactionType",
				"CardEntryMode", "TransactionTypeCode", "TransactionSequenceNumber", "CardType", "SubCardType",
				"TotalApprovedAmount", "ResponseText", "ResponseCode", "TransactionIdentifier", "AurusPayTicketNum",
				"ApprovalCode", "Primary processor Failover counter", "ProcessorMerchantId", "NOTES");

	}

	public void setupWorkBook() {
		if (workbook == null) {
			workbook = new XSSFWorkbook();
			sheet = workbook.createSheet("Transactions");
		}
	}

	public void writeTransactionData(List<String> data) {
		if (this.sheet == null) {
			setupWorkBook();
		}

		int currentRow = sheet.getLastRowNum() + 1;
		int currentColumn = 0;

		if (currentRow == 0) {

			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < parameters.size(); i++) {
				headerRow.createCell(i).setCellValue(parameters.get(i));
				// Create a cell style for header cells
				CellStyle headerCellStyle = workbook.createCellStyle();
				Font headerFont = workbook.createFont();
				headerFont.setBold(true); // Make the header text bold
				headerFont.setColor(IndexedColors.WHITE.getIndex()); // Set the font color to blue
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex()); // Set the fill color
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Set the fill pattern

				// Apply the header cell style to the current cell
				headerRow.getCell(i).setCellStyle(headerCellStyle);
			}
		}

		Row row = sheet.createRow(currentRow + 1);
		for (int i = 0; i < data.size(); i++) {
			Cell cell = row.createCell(currentColumn++);
			String value = data.get(i);
			cell.setCellValue(value);

			// Apply cell styles
			if (i == 11 && "APPROVAL".equalsIgnoreCase(value)) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.GREEN.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			} else if (i == 11) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.RED.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			}

			if (i == 12 && value.equalsIgnoreCase("00000")) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.GREEN.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			} else if (i == 12) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.RED.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			}

		}
	}

	public void writeHeadline(String value) {
		if (this.sheet == null) {
			setupWorkBook();
		}

		int currentRow = sheet.getLastRowNum() + 1;

		if (currentRow == 0) {

			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < parameters.size(); i++) {
				headerRow.createCell(i).setCellValue(parameters.get(i));
				// Create a cell style for header cells
				CellStyle headerCellStyle = workbook.createCellStyle();
				Font headerFont = workbook.createFont();
				headerFont.setBold(true); // Make the header text bold
				headerFont.setColor(IndexedColors.WHITE.getIndex()); // Set the font color to blue
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex()); // Set the fill color
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Set the fill pattern

				// Apply the header cell style to the current cell
				headerRow.getCell(i).setCellStyle(headerCellStyle);
			}
		}
		int lastRowNum = sheet.getLastRowNum() + 2;

		// If no rows exist, create a new row
		if (lastRowNum == -1) {
			lastRowNum = 0;
		} else {
			lastRowNum += 1; // Increment to get the next row number
		}

		Row newRow = sheet.createRow(lastRowNum);
		sheet.addMergedRegion(new CellRangeAddress(lastRowNum, lastRowNum, 0, 18));
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Cell newCell = newRow.createCell(0);
		newCell.setCellValue(value);
		newCell.setCellStyle(style);
	}

	public void saveExcelFile(String fileName) {
		try (FileOutputStream outputStream = new FileOutputStream("./transactionsXLfiles/" + fileName)) {
			workbook.write(outputStream);
			System.out.println("=".repeat(150));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
