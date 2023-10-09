package ru.researchser.services.parser.logic.outputFile;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelFile implements ExportAlgorithm{

    @Override
    public void exportData (List<String> header, HashMap<String, List<String>> allPagesParseResult, String pathToOutput) {
        XSSFWorkbook excelFile = new XSSFWorkbook();
        XSSFSheet spreadsheet = excelFile.createSheet("Researchser");
        fillHeader(spreadsheet, header);
        int rowNum = 1;
        for(Map.Entry<String, List<String>> entry : allPagesParseResult.entrySet()) {
            XSSFRow row = spreadsheet.createRow(rowNum);
            List<String> infoList = new ArrayList<>(entry.getValue());
            infoList.add(0, entry.getKey());
            fillRowWithData(infoList, row);
            System.out.printf("Wrote %s row%n", rowNum);
            rowNum++;
        }
        saveXlsx(excelFile, pathToOutput);
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
    private void saveXlsx(XSSFWorkbook excelFile, String pathToOutput) {
        try (FileOutputStream outputFile = new FileOutputStream((pathToOutput))) {
            excelFile.write(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Неверный путь к файлу");
        }
    }

}
