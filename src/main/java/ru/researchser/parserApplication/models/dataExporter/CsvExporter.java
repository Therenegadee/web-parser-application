package ru.researchser.parserApplication.models.dataExporter;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class CsvExporter {

    public void exportCSV(List<String> header, List<List<String>> allPagesParseResult) {
        Scanner scanner = new Scanner(System.in);
        FileWriter outputFile = null;
        do {
            System.out.println("Введите путь к сохранению файла: ");
            String pathToOutput = scanner.nextLine();
            File file = new File(pathToOutput);
            try {
                outputFile = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Неверный путь к файлу");
            }
        } while (outputFile == null);

        CSVWriter writer = new CSVWriter(outputFile, ';');
        writer.writeNext(header.toArray(String[]::new));
        int i = 0;
        for (List<String> infoList : allPagesParseResult) {
            writer.writeNext(infoList.toArray(String[]::new));
            System.out.printf("Wrote %s row%n", i);
            i++;
        }
    }
}
