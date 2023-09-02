package ru.researchser.parserApplication.models.dataExporter;

import java.util.List;

public interface ExportAlgorithm {
    void exportData(List<String> header, List<List<String>> allPagesParseResult, String pathToOutput);
}
