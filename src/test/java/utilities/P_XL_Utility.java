package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P_XL_Utility {

    Workbook workbook;
    Sheet sheet;

    public void setupWorkBook() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Transactions");
    }

    public void writeDataForVoid(List<String> data) {
        if (this.sheet == null) {
            setupWorkBook();
        }

        int currentRow = sheet.getLastRowNum() + 1;

        if (currentRow == 0) {
            Row headerRow = sheet.createRow(0);
            // Add headers here based on your requirement
            List<String> headers = Arrays.asList("TransId", "AurusPayTicketNumber", "amount", "transType", "PMI");
            for (int i = 0; i < headers.size(); i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(headers.get(i));
            }
            currentRow++;
        }

        Row row = sheet.createRow(currentRow);
        for (int i = 0; i < data.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data.get(i));
        }
    }

    public void saveExcelFile() {
		try (FileOutputStream outputStream = new FileOutputStream("./test-Data\\VoidTransactions.xlsx")) {
			workbook.write(outputStream);
			System.out.print(
					"++".repeat(1));
		} catch (IOException e) {
			e.printStackTrace();  
		}
	}

   
}
