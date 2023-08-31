package ru.researchser.parserApplication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import ru.researchser.parserApplication.configs.ParserApplicationContextConfiguration;
import ru.researchser.parserApplication.models.dataExporter.CsvExporter;
import ru.researchser.parserApplication.models.dataExporter.ExcelExporter;
import ru.researchser.parserApplication.models.settingsForParsing.LinksExtractor;
import ru.researchser.parserApplication.settingsForParsing.*;
import ru.researchser.parserApplication.models.htmlElementParser.ParseElement;

import org.openqa.selenium.WebDriver;
import ru.researchser.parserApplication.services.ParserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@Import(ParserApplicationContextConfiguration.class)
@RequiredArgsConstructor
public class ParserApplication {

    private WebDriver driver;
    private LinksExtractor linksExtractor;
    private ExcelExporter excelExporter;
    private CsvExporter csvExporter;
    private ParserService parserService;
    private ParseElement parseElement;


    private static final Scanner SCANNER = new Scanner(System.in);
    private final List<List<String>> allPagesParseResult = new ArrayList<>();
    private final List<String> header = new ArrayList<>();

    public void runParser() {
        List<String> linksToPagesForParse;
        List<ParseElement> parsingTypes;

        System.out.println("Введите ссылку на каталог страниц, которые нужно спарсить");
        String firstPageURL = SCANNER.nextLine(); // https://zhongchou.modian.com/all/top_comment/all/1
        driver.get(firstPageURL);

        linksToPagesForParse = linksExtractor.getPagesToParseLinks(driver);

        parsingTypes = parseElement.addElementsToParse(driver, header);


        int parsePageNumber = 0;
        for (String link : linksToPagesForParse) {
            parsePageNumber++;
            System.out.printf("Парсим информацию со сыылки № %d", parsePageNumber);
            System.out.println();
            driver.get(link);
            List<String> pageParseResult = new ArrayList<>();
            for (ParseElement parseElement : parsingTypes) {
                pageParseResult.add(parseElement.parseByParameters(link));
            }
            allPagesParseResult.add(pageParseResult);
            parserService.saveParsingResult(pageParseResult, link);
        }
        System.out.println("Парсинг закончен.");

        boolean outputFormatNotChosen = true;
        while (outputFormatNotChosen) {
            System.out.println("Введите формат файла вывода: xlsx или csv: ");
            String chooseOutputFormat = SCANNER.next();
            if (chooseOutputFormat.equalsIgnoreCase("xlsx")) {
                outputFormatNotChosen = false;
                excelExporter.exportXlsx(header, allPagesParseResult);
            } else if (chooseOutputFormat.equalsIgnoreCase("csv")) {
                outputFormatNotChosen = false;
                csvExporter.exportCSV(header, allPagesParseResult);
            } else {
                System.err.println("Неверно введен формат.");
            }
        }
        driver.quit();
    }
}
