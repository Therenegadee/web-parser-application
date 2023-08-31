package ru.researchser.parserApplication.models.dataExporter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class ExcelExporter {

    private final Scanner SCANNER = new Scanner(System.in);

    public void exportXlsx (List<String> header, List<List<String>> allPagesParseResult) {
        XSSFWorkbook excelFile = new XSSFWorkbook();
        XSSFSheet spreadsheet = excelFile.createSheet("Researchser");
        fillHeader(spreadsheet, header);
        int rowNum = 1;
        for(List<String> infoList : allPagesParseResult) {
            XSSFRow row = spreadsheet.createRow(rowNum);
            fillRowWithData(infoList, row);
            System.out.printf("Wrote %s row%n", rowNum);
            rowNum++;
        }
        saveXlsx(excelFile);
    }

    private void fillRowWithData (List<String> infoList, XSSFRow row) {
        for (int i = 0; i < infoList.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(infoList.get(i));
        }
    }

    private void fillHeader(XSSFSheet spreadsheet, List<String> header) {
        XSSFRow row;
        row = spreadsheet.createRow(0);
        for (int i = 0; i < header.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header.get(i));
        }
        System.out.println("Header filled.");

    }
    private void saveXlsx(XSSFWorkbook excelFile) {
        do {
            System.out.println("Введите путь к куда сохранить файл: ");
            String pathToOutput = SCANNER.nextLine();
            try (FileOutputStream outputFile = new FileOutputStream((pathToOutput))) {
                excelFile.write(outputFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Неверный путь к файлу");
            }
        } while (false);
    }

}
