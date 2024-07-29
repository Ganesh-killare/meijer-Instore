package utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

public class ExcelDataWriting {
	Workbook workbook;
	static Sheet sheet;

	int startRow = 1;
	List<CellRangeAddress> mergedRegions = new ArrayList<>();

	@BeforeSuite
	public void setupWorkBook() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Void_Of_Sale");

	}

	@Test(invocationCount = 10)
	public void testwriteData() throws IOException {
		// Write GCB data
		List<String> GCBData = Arrays.asList("CardToken", "CRMToken", "CIToken", "EntryMode", "TransType", "SeqNum",
				"CardType", "SubCardType", "ApproveAmount", "ResponseText", "00000", "TicketNumber", "ApprovalCode",
				"Transaction ID"); // transactionType value hardcoded
		List<String> GCBData1 = Arrays.asList("CardToken", "CRMToken", "CIToken", "Sale", "EntryMode",
				"TransType", "SeqNum", "CardType", "SubCardType", "ApproveAmount", "Approve", "ResponseCode",
				"TicketNumber", "ApprovalCode", "Transaction ID");
		List<String> GCBData2 = Arrays.asList("CardToken", "CRMToken", "CIToken", "Refund", "EntryMode",
				"TransType", "SeqNum", "CardType", "SubCardType", "ApproveAmount", "ResponseText", "ResponseCode",
				"TicketNumber", "ApprovalCode", "Transaction ID");

		WriteGCBData(GCBData);

		// Write Sale data after GCB data

		writeData(GCBData1);

		// Write Refund data after Sale data
		writeData(GCBData2);
	}

	public void WriteGCBData(List<String> data) {
		if (startRow == 1) {

			
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("CardToken");
			headerRow.createCell(1).setCellValue("CRMToken");
			headerRow.createCell(2).setCellValue("CIToken");
			headerRow.createCell(3).setCellValue("Transaction Type");
			headerRow.createCell(4).setCellValue("EntryMode");
			headerRow.createCell(5).setCellValue("TransType");
			headerRow.createCell(6).setCellValue("SeqNum");
			headerRow.createCell(7).setCellValue("CardType");
			headerRow.createCell(8).setCellValue("SubCardType");
			headerRow.createCell(9).setCellValue("ApproveAmount");
			headerRow.createCell(10).setCellValue("ResponseText");
			headerRow.createCell(11).setCellValue("ResponseCode");
			headerRow.createCell(12).setCellValue("AurusPayTicketNumber");
			headerRow.createCell(13).setCellValue("ApprovalCode");
			headerRow.createCell(14).setCellValue("Transaction_ID");
			
			
			   // Create a cell style for header cells
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true); // Make the header text bold
	        headerFont.setColor(IndexedColors.WHITE.getIndex()); // Set the font color to blue
	        headerCellStyle.setFont(headerFont);
	        headerCellStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex()); // Set the fill color
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Set the fill pattern

			for (int i = 0; i <= 14; i++) {
				 headerRow.getCell(i).setCellStyle(headerCellStyle); // Apply the header cell style
			}
		}

		List<String> cardTokens = generateCardTokens(data.get(0));
		List<String> CRMTokens = generateCRMTokens(data.get(1));
		List<String> CITokens = generateCITokens(data.get(2));

		int rowIndex = startRow;
		int round = 1;
		for (int i = 0; i < cardTokens.size(); i++) {
			Row row = sheet.createRow(rowIndex);

			Cell cardTokenCell = row.createCell(0);
			Cell CRMTokenCell = row.createCell(1);
			Cell CITokenCell = row.createCell(2);

			int endRow = rowIndex + 3;

			if (!isRegionMerged(rowIndex, 0, endRow, 0)) {
				mergeCells(sheet, rowIndex, endRow, 0);
			}

			if (!isRegionMerged(rowIndex, 1, endRow, 1)) {
				mergeCells(sheet, rowIndex, endRow, 1);
			}
			if (!isRegionMerged(rowIndex, 2, endRow, 2)) {
				mergeCells(sheet, rowIndex, endRow, 2);
			}

			cardTokenCell.setCellValue(cardTokens.get(i));
			CRMTokenCell.setCellValue(CRMTokens.get(i));
			CITokenCell.setCellValue(CITokens.get(i));

			row.createCell(3).setCellValue("GCB");
			row.createCell(4).setCellValue(data.get(3));

			row.createCell(5).setCellValue(data.get(4));
			row.createCell(6).setCellValue(data.get(5));
			row.createCell(7).setCellValue(data.get(6));
			row.createCell(8).setCellValue(data.get(7));
			row.createCell(9).setCellValue(data.get(8));

			// row.createCell(10).setCellValue(data.get(9));

			// Set cell style for cell 10 based on "Approve" or "Reject"
			if (data.get(9).equalsIgnoreCase("Approve")) {
				CellStyle approveCellStyle = workbook.createCellStyle();
				Font approveFont = workbook.createFont();
				approveFont.setBold(true);
				approveFont.setColor(IndexedColors.GREEN.getIndex()); // Green for "Approve"
				approveCellStyle.setFont(approveFont);
				row.createCell(10).setCellValue(data.get(9));
				row.getCell(10).setCellStyle(approveCellStyle);
			} else {
				CellStyle rejectCellStyle = workbook.createCellStyle();
				Font rejectFont = workbook.createFont();
				rejectFont.setBold(true);
				rejectFont.setColor(IndexedColors.RED.getIndex()); // Red for "Reject"
				rejectCellStyle.setFont(rejectFont);
				row.createCell(10).setCellValue(data.get(9));
				row.getCell(10).setCellStyle(rejectCellStyle);
			}

			// Set cell style for cell 11 based on "00000" or non-"00000"
			if (data.get(10).equalsIgnoreCase("00000")) {
				CellStyle zeroCellStyle = workbook.createCellStyle();
				Font zeroFont = workbook.createFont();
				zeroFont.setBold(true);
				zeroFont.setColor(IndexedColors.GREEN.getIndex()); // Green for "00000"
				zeroCellStyle.setFont(zeroFont);
				row.createCell(11).setCellValue(data.get(10));
				row.getCell(11).setCellStyle(zeroCellStyle);
			} else {
				CellStyle nonZeroCellStyle = workbook.createCellStyle();
				Font nonZeroFont = workbook.createFont();
				nonZeroFont.setBold(true);
				nonZeroFont.setColor(IndexedColors.RED.getIndex()); // Red for non-"00000"
				nonZeroCellStyle.setFont(nonZeroFont);
				row.createCell(11).setCellValue(data.get(10));
				row.getCell(11).setCellStyle(nonZeroCellStyle);
			}

			row.createCell(12).setCellValue(data.get(11));
			row.createCell(13).setCellValue(data.get(12));
			row.createCell(14).setCellValue(data.get(13));

		}

		if (round == 3) {
			rowIndex++;
			round = 0;
		}

		rowIndex++;
		round++;
		startRow = rowIndex + 3;
	}

	private static List<String> generateCardTokens(String cardToken) {
		List<String> cardTokens = new ArrayList<>();
		if (cardToken != null) {
			cardTokens.add("");
			cardTokens.add("");
			cardTokens.add(cardToken);
		} else {
			cardTokens.add("");
			cardTokens.add("");
			cardTokens.add("Null");
		}
		return cardTokens;
	}

	private static List<String> generateCRMTokens(String CRMToken) {
		List<String> CRMTokens = new ArrayList<>();
		if (CRMToken != null) {
			CRMTokens.add("");
			CRMTokens.add("");
			CRMTokens.add(CRMToken);
		} else {
			CRMTokens.add("");
			CRMTokens.add("");
			CRMTokens.add("NULL");
		}
		return CRMTokens;
	}

	private static List<String> generateCITokens(String CIToken) {
		List<String> CITokens = new ArrayList<>();
		if (CIToken != null) {
			CITokens.add("");
			CITokens.add("");
			CITokens.add(CIToken);
		} else {
			CITokens.add("");
			CITokens.add("");
			CITokens.add("NULL");
		}
		return CITokens;
	}

	private void mergeCells(Sheet sheet, int startRow, int endRow, int col) {
		CellRangeAddress cellRange = new CellRangeAddress(startRow, endRow, col, col);
		sheet.addMergedRegion(cellRange);
		mergedRegions.add(cellRange);
	}

	private boolean isRegionMerged(int firstRow, int firstCol, int lastRow, int lastCol) {
		for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
			if (mergedRegion.getFirstRow() <= lastRow && mergedRegion.getLastRow() >= firstRow
					&& mergedRegion.getFirstColumn() <= lastCol && mergedRegion.getLastColumn() >= firstCol) {
				return true;
			}
		}
		return false;
	}

	private void writeData(List<String> data) {
		int currentRow = sheet.getLastRowNum() + 1;
		int currentColumn = 3;

		Row row = sheet.createRow(currentRow);
		for (int i = 3; i < data.size(); i++) {
			Cell cell = row.createCell(currentColumn++);
			String value = data.get(i);
			cell.setCellValue(value);

			if (i == 0 && "Sale".equalsIgnoreCase(value) || i == 0 && "Refund".equalsIgnoreCase(value)) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.BLACK.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			}

			if (i == 10 && "Approve".equalsIgnoreCase(value)) {
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

			if (i == 11 && !"00000".equals(value)) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.RED.getIndex());
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

	@AfterSuite
	public void saveExcelFile() {
		try (FileOutputStream outputStream = new FileOutputStream("Void_Of_Sale.xlsx")) {
			workbook.write(outputStream);
			System.out.println("Excel file created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
