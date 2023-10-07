package ru.researchser.parser.logic.outputFile;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
public class OutputFile {
    private ExportAlgorithm exportAlgorithm;
    private OutputFileType type;

    public OutputFile(OutputFileType type) {
        this.type=type;
        this.exportAlgorithm = switch (type){
            case XLSX -> new ExcelFile();
            case CSV -> new CsvFile();
        };
    }

    public void exportData(List<String> header, HashMap<String, List<String>> allPagesParseResult, String pathToOutput) {
        exportAlgorithm.exportData(header, allPagesParseResult, pathToOutput);
    }
}
