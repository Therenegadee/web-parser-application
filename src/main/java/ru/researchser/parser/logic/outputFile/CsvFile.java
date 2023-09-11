package ru.researchser.parser.logic.outputFile;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class CsvFile implements ExportAlgorithm {

    @Override
    public void exportData(List<String> header, List<List<String>> allPagesParseResult, String pathToOutput) {
        FileWriter outputFile = null;
        do {
            System.out.println("Введите путь к сохранению файла: ");
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
