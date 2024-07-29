package utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

public class GiftDataXLwriting {

	Workbook workbook;
	Sheet sheet;

	int startRow = 1;
	List<CellRangeAddress> mergedRegions = new ArrayList<>();

	public void setupWorkBook() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("GIFT");

	}

	public void WriteActivationData(List<String> data, List<String> parameters) {
		data.add(3, "Pre-Auth Activation");
		if (this.sheet == null) {
			setupWorkBook();
		}

		if (startRow == 1) {
			parameters.add(3, "TransactionType");

			Row headerRow = sheet.createRow(0);

			for (int i = 0; i < parameters.size(); i++) {

				headerRow.createCell(i).setCellValue((String) parameters.get(i));

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

			int endRow = rowIndex + 5;

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
			

			int currentColumn = 3;
			for (int j = 3; j < data.size(); j++) {

				Cell cell = row.createCell(currentColumn++);
				String value = data.get(j);
				cell.setCellValue(value);

				if (j == 3 && !value.isEmpty()) {
					CellStyle cellStyle = workbook.createCellStyle();
					Font font = workbook.createFont();
					font.setBold(true);
					font.setColor(IndexedColors.BLACK.getIndex());
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
				}

				if (j == 11 && "APPROVAL".equalsIgnoreCase(value)) {
					CellStyle cellStyle = workbook.createCellStyle();
					Font font = workbook.createFont();
					font.setBold(true);
					font.setColor(IndexedColors.GREEN.getIndex());
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
				} else if (j == 11) {
					CellStyle cellStyle = workbook.createCellStyle();
					Font font = workbook.createFont();
					font.setBold(true);
					font.setColor(IndexedColors.RED.getIndex());
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
				}

				if (j == 12 && value.equalsIgnoreCase("00000")) {
					CellStyle cellStyle = workbook.createCellStyle();
					Font font = workbook.createFont();
					font.setBold(true);
					font.setColor(IndexedColors.GREEN.getIndex());
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
				} else if (j == 12) {
					CellStyle cellStyle = workbook.createCellStyle();
					Font font = workbook.createFont();
					font.setBold(true);
					font.setColor(IndexedColors.RED.getIndex());
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
				}
			}
		}

		if (round == 5) {
			rowIndex++;
			round = 0;
		}

		rowIndex++;
		round++;
		startRow = rowIndex + 5;
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

	public void writeDataRefundOfSale(List<String> data) {
		int currentRow = sheet.getLastRowNum() + 1;
		int currentColumn = 3;

		Row row = sheet.createRow(currentRow);
		for (int i = 3; i < data.size(); i++) {

			Cell cell = row.createCell(currentColumn++);
			String value = data.get(i);
			cell.setCellValue(value);

			if (i == 3 && !value.isEmpty()) {
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.BLACK.getIndex());
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
			}

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

	public void saveExcelFile(String fileName) {
		try (FileOutputStream outputStream = new FileOutputStream("./transactionsXLfiles\\" + fileName)) {
			workbook.write(outputStream);
			System.out.println(
					"============================================================================================================================================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
