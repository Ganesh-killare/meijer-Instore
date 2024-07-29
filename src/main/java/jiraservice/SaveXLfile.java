package jiraservice;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveXLfile {
    public static void main(String[] args) {
        String logFilePath = "./input.txt"; // Update with your input file path
        String excelFilePath = "./output.xlsx"; // Update with your output Excel file path

        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            String line;
            int rownum = 0;
            Row row = null;
            boolean isClosed = true;

            while ((line = br.readLine()) != null) {
                if (isClosed) {
                    row = sheet.createRow(rownum++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(line);
                    if (line.contains("CloseTransactionResponse")) {
                        isClosed = false;
                    }
                } else {
                    if (line.contains("CloseTransactionResponse")) {
                        isClosed = true;
                    } else {
                        Cell cell = row.createCell(0);
                        cell.setCellValue(cell.getStringCellValue() + "\n" + line);
                    }
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
                workbook.close();
                System.out.println("Excel file has been created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
