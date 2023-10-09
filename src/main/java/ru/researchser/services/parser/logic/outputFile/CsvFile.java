package ru.researchser.services.parser.logic.outputFile;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvFile implements ExportAlgorithm {

    @Override
    public void exportData(List<String> header, HashMap<String, List<String>> allPagesParseResult, String pathToOutput) {
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
        for (Map.Entry<String, List<String>> entry : allPagesParseResult.entrySet()) {
            List<String> infoList = new ArrayList<>(entry.getValue());
            infoList.add(0, entry.getKey());
            writer.writeNext(infoList.toArray(String[]::new));
            System.out.printf("Wrote %s row%n", i);
            i++;
        }
    }
}
