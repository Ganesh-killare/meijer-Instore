package utilities;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import base.BaseClass;

// This class calculates transaction time
public class LogsParserAdvance extends BaseClass {     

	private String ReqOfPOS = "PED REQUEST : "; // " Request Received From POS :";
	private String ResOfPOS = "TransResponse : <TransResponse><CCTID>01</CCTID>";// " POS response sent successfully.";

	private String ReqOfPED = null;
	private String ResOfPED = null;

	private static final String LOG_FILE_PATH = "./src/test/resources/AESDK.log";

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
	private static final Pattern TIMESTAMP_PATTERN = Pattern  
			.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})");

	private Workbook workbook;
	private Sheet sheet;

	public static void main(String[] args) throws Exception {   
		LogsParserAdvance lp = new LogsParserAdvance(); 
		lp.setupWorkBook();
		lp.parseLogFile(LOG_FILE_PATH);
		
		lp.saveExcelFile("LogTransactions");   
	}

	private void setupWorkBook() {   
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Transactions");     
		createHeaderRow();
	}

	private void createHeaderRow() {
	    // Create a header row
	    Row headerRow = sheet.createRow(0);

	    // Create a list of headers
	    List<String> headers = new ArrayList<>(Arrays.asList(parameters));    
	    headers.add(1, "TransactionType");
	    headers.add("Total Sale Time ");

	    // Create a cell style for the header
	    CellStyle headerStyle = workbook.createCellStyle();
    
	    // Set the background color to dark green
	    headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);  

	    // Create a font and set the font color to white
	    Font font = workbook.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setBold(true);   
	    headerStyle.setFont(font);

	    // Apply the style to each header cell
	    for (int i = 0; i < headers.size(); i++) {
	        Cell headerCell = headerRow.createCell(i);
	        headerCell.setCellValue(headers.get(i));
	        headerCell.setCellStyle(headerStyle);
	    }
	}

	private void parseLogFile(String logFilePath) throws Exception {
		System.out.println("Parsing log file: " + logFilePath);
		String Request = null ;

		try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
			String line;
			LocalDateTime requestReceivedTimestamp = null;

			String cardToken = null;

			while ((line = br.readLine()) != null) {
				Matcher matcher = TIMESTAMP_PATTERN.matcher(line);
				if (matcher.find()) {
					LocalDateTime timestamp = LocalDateTime.parse(matcher.group(), FORMATTER);
					String messageType = getMessageType(line);

					if (messageType != null) {
						if (messageType.equals(ReqOfPOS) || messageType.equals(ReqOfPED)) {
							requestReceivedTimestamp = timestamp;
						 Request = extractRequestType(line);
							/*
							 * LogResponse LR = new LogResponse(Request); cardToken =
							 * LR.getParameterValue("CardToken");
							 */
							cardToken=null;

						} else if (messageType.equals(ResOfPOS) || messageType.equals(ResOfPED)) {
							if (requestReceivedTimestamp != null) {
								String Response = extractRequestType(line);
								String duration = formatTime(Duration.between(requestReceivedTimestamp, timestamp).toString());
								processTransaction(Response, cardToken, duration);
								System.out.println("Request Return to PED "+convertDateTimeFormat(requestReceivedTimestamp.toString()));
								System.out.println(timestamp + "    " +Response);
								System.out.println(duration);
								requestReceivedTimestamp = null;  
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading the log file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void processTransaction(String response, String cardToken, String diff) throws Exception {
	response.trim();
		LogResponse SaleParam = new LogResponse(response);

		List<String> saleData = SaleParam.print_Response(" Sale  ", parameters , cardToken);
	
		saleData.set(0, cardToken);

		saleData.add(diff);   
	
		writeTransactionData(saleData);

	}

	private String getMessageType(String line) {

		// for the END TO END transaction time

		if (line.contains(ReqOfPOS)) {
			return ReqOfPOS;
		} else if (line.contains(ResOfPOS)) {
			return ResOfPOS;
		}

		// For the CCT transaction Time
		/*
		 * if (line.contains(ReqOfPED)) { return ReqOfPED; } else if
		 * (line.contains(ResOfPED)) { return ResOfPED; }
		 */

		return null;
	}

	/*
	 * private String extractRequestType(String line) { int startIndex =
	 * line.indexOf(": < ") + 3; // Add 3 to include the length of ": < " int
	 * endIndex = line.indexOf('>', startIndex); // Look for '>' after ": < " if
	 * (endIndex == -1) { endIndex = line.length(); } return line.substring(131,
	 * endIndex); //return ""; }
	 */
	
	
	public String formatTime(String timeString) {
	    // Check if the input string is not null and starts with "PT"
	    if (timeString != null && timeString.startsWith("PT")) {
	        // Extract the numeric part of the time string
	        String numericPart = timeString.substring(2, timeString.length() - 1);
	        
	        // Convert the numeric part to seconds
	        double seconds = Double.parseDouble(numericPart);
	        
	        // Return the formatted time string
	        return String.format("%.3f sec", seconds);
	    } else {
	        return "Invalid time format";
	    }
	}

	private String extractRequestType(String line) {
		int startIndex = line.indexOf('<');
		if (startIndex == -1) {
			return ""; // If '<' is not found, return an empty string
		}
		return line.substring(startIndex); // Extract from '<' to the end of the line
	}

	private void writeTransactionData(List<String> data) {  
		int currentRow;
	

		if (this.sheet == null) {
			setupWorkBook();
		}

		currentRow = sheet.getLastRowNum() + 1;

		int currentColumn = 0;

		Row row = sheet.createRow(currentRow);
		for (int i = 0; i < data.size(); i++) {

			Cell cell = row.createCell(currentColumn++);
			String value = data.get(i);
			cell.setCellValue(value);

			if (i == 1) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.BLACK.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			}

			if (i == 10 && "APPROVAL".equalsIgnoreCase(value)) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.GREEN.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			} else if (i == 10) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.RED.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			}

			if (i == 11 && value.equalsIgnoreCase("00000")) {
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

		}

	}
	 public static String convertDateTimeFormat(String dateTime) {
	        // Replace 'T' with a space
	        String modifiedDateTime = dateTime.replace("T", " ");

	        // Replace dot '.' with comma ','
	        modifiedDateTime = modifiedDateTime.replace(".", ",");

	        return modifiedDateTime;
	    }

	private void saveExcelFile(String fileName) {
		try (FileOutputStream outputStream = new FileOutputStream(
				"./transactionsXLfiles\\" + "LogParcerdata.xlsx")) {     
			workbook.write(outputStream);
			System.out.println("=".repeat(150));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
