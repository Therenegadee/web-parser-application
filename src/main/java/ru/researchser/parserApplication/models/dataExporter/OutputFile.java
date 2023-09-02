package ru.researchser.parserApplication.models.dataExporter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class OutputFile {
    private ExportAlgorithm exportAlgorithm;
    private OutputFileType type;

    public OutputFile(OutputFileType type) {
        this.exportAlgorithm = switch (type){
            case XLSX -> new ExcelFile();
            case CSV -> new CsvFile();
        };
    }

    public void exportData(List<String> header, List<List<String>> allPagesParseResult, String pathToOutput) {
        exportAlgorithm.exportData(header, allPagesParseResult, pathToOutput);
    }
}
